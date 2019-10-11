package com.cold.controller;

import com.cold.Constants;
import com.cold.client.IMachineTranslationService;
import com.cold.dto.FileObj;
import com.cold.dto.TaskStatus;
import com.cold.dto.TaskType;
import com.cold.entity.SysUser;
import com.cold.entity.TBCorpusFileUploaded;
import com.cold.entity.TBUserTask;
import com.cold.entity.TBUserTaskFile;
import com.cold.filters.XLIFFFilter;
import com.cold.page.Pager;
import com.cold.searchservice.client.ISearchService;
import com.cold.service.ICorpusService;
import com.cold.service.ITaskService;
import com.cold.tmx.TmxParse;
import com.cold.util.ContextUtil;
import com.cold.util.FileUtil;
import com.cold.util.ZipUtil;
import com.cold.vo.CorpusMatchVo;
import com.cold.vo.UserTaskVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/8/13 16:01
 * @Description:
 */
@Controller
@Slf4j
@RequiresRoles("ROLE_TRANS")
@RequestMapping("corpus")
public class CorpusController extends BaseController {

    @Autowired
    private ICorpusService corpusService;
    @Resource
    private IMachineTranslationService machineTranslationService;
    @Resource
    private ISearchService searchService;
    @Autowired
    private ITaskService taskService;
    @RequestMapping("corpusmatch")
    public String corpusmatch(Model model){
        Long userId = ContextUtil.getUserId();
        List<UserTaskVo> taskUserReceives = taskService.getTaskUserReceiveList(userId,null);
        model.addAttribute("taskUserReceives",taskUserReceives);
        return "corpus/corpus_match";
    }

    @RequestMapping(value = "corpusmatchList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> corpusmatchList(CorpusMatchVo vo){
        Pager pager = new Pager();
        pager.setPageNo(vo.getPageNo());
        if(vo.getPageSize()!=null)pager.setPageSize(vo.getPageSize());
        vo.setCreateUserId(ContextUtil.getUserId());
        pager.setCondition(vo);
        corpusService.getPagerCorpus(pager);
        return jsonResult(true,pager);
    }

    @RequestMapping(value = "/uploadTaskFile",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadTaskFile(MultipartFile file,Long userTaskId, HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        Map<String, Object> json = new HashMap();
        if(file==null){
            json.put("success",false);
            json.put("msg","没有选择文件");
            return json;
        }
        if(userTaskId==null){
            json.put("success",false);
            json.put("msg","没有选择任务");
            return json;
        }
        System.out.println("任务id:"+userTaskId);
        String uuid = corpusService.getUuid();
        String xliffpath = FileUtil.getDatePath()+File.separator+Constants.XLIFFPATH;
        Long userId = ContextUtil.getUserId();
        String basePath = getBaseDir();
        String username = ContextUtil.getLoginUser().getUsername();
        String path = username+ File.separator+ xliffpath; //文件上传路径信息
        String dir = basePath+File.separator+path;
        FileUtil.mkDirs(dir);

        String originalFileName = file.getOriginalFilename();
        String filename = uuid + "." + FileUtil.getFileType(originalFileName);
        String processResFileName = uuid+"_rs"+ "." + FileUtil.getFileType(originalFileName);
        String filePath =  File.separator + path+File.separator+filename;
        String prccessPath = File.separator + path+File.separator+processResFileName;
        File saveFile = new File(basePath,filePath);
        FileOutputStream fos = new FileOutputStream(saveFile);
        byte[] bytes = file.getBytes();
        fos.write(bytes);
        fos.flush();
        fos.close();


        TBCorpusFileUploaded corpusFileUploaded = new TBCorpusFileUploaded();
        corpusFileUploaded.setCreateUserId(userId);
        corpusFileUploaded.setFilename(originalFileName);
        corpusFileUploaded.setFilePath(filePath);
        corpusFileUploaded.setUploadTime(new Date());
        corpusFileUploaded.setStatus(Constants.Status.STATUS_PROCESS);
        corpusFileUploaded.setResFilePath(prccessPath);
        String resultName = FileUtil.getFileName(originalFileName)+"_rs."+FileUtil.getFileType(originalFileName);
        corpusFileUploaded.setRsFilename(resultName);
        TBUserTask tbUserTask = corpusService.findEntityById(TBUserTask.class,userTaskId);
        corpusFileUploaded.setUserTask(tbUserTask);
        corpusFileUploaded.setTaskNo(tbUserTask.getTaskNo());
        //corpusFileUploaded.setTbTask(tbUserTask.getTbTask());
        corpusService.save(corpusFileUploaded);
        //Long id = corpusFileUploaded.getFileId();
        new Thread(()->{
            XLIFFFilter xliffFilter = new XLIFFFilter(machineTranslationService,searchService);
            Map<String,Object> resMap = xliffFilter.parse(saveFile.getAbsolutePath(),basePath+prccessPath);
            boolean success = (boolean) resMap.get("success");
            if(success){
                corpusFileUploaded.setStatus(Constants.Status.STATUS_COMPLETE);
                String tmxFilePath = resMap.get("tmxfile").toString();
                File tmxFile = new File(tmxFilePath);
                if(tmxFile.exists()){
                    FileObj xliffObj = new FileObj(basePath+prccessPath,originalFileName);
                    FileObj tmxObj = new FileObj(tmxFilePath,tmxFile.getName());
                    List<FileObj> fileObjs = Lists.newArrayList(xliffObj,tmxObj);
                    String zipName = FileUtil.getFileName(originalFileName)+"_rs.zip";
                    String rsPath = File.separator + path+File.separator+uuid+"_rs.zip";
                    corpusFileUploaded.setRsFilename(zipName);
                    corpusFileUploaded.setResFilePath(rsPath);
                    String zipFile = basePath+rsPath;
                    try {
                        ZipUtil.zip(zipFile,fileObjs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{
                String msg = resMap.get("msg").toString();
                corpusFileUploaded.setStatus(Constants.Status.STATUS_ERROR);
                corpusFileUploaded.setProcessMsg(msg);
            }
            corpusFileUploaded.setFinishedTime(new Date());
            corpusService.update(corpusFileUploaded);
        }).start();
        return jsonResult(true,"");
    }

    @RequestMapping(value = "/deleteCorpusFile")
    @ResponseBody
    public Map<String,Object> deleteCorpusFile(Long fileId){
        TBCorpusFileUploaded fileUploaded = corpusService.findEntityById(TBCorpusFileUploaded.class,fileId);
        String basePath = getBaseDir();
        File file = new File(basePath,fileUploaded.getFilePath());
        File resFile = new File(basePath,fileUploaded.getResFilePath());
        FileUtils.deleteQuietly(file);
        FileUtils.deleteQuietly(resFile);
        corpusService.delete(fileUploaded);
        return jsonResult(true,"删除成功");
    }
    @RequestMapping(value = "/downCorpusFile/{fileId}")
    @ResponseBody
    public void downCorpusFile(@PathVariable Long fileId, HttpServletResponse response){
        TBCorpusFileUploaded fileUploaded = corpusService.findEntityById(TBCorpusFileUploaded.class,fileId);
        String basePath = getBaseDir();
        File file = new File(basePath,fileUploaded.getResFilePath());
        downloadFile(response,file,fileUploaded.getRsFilename());
    }

    @RequestMapping(value = "checkUserTasks",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> checkUserTasks(){
        Long userId = ContextUtil.getUserId();
        List<TBUserTask> taskUserReceives = taskService.getTaskUserReceives(userId,null);
        return jsonResult(taskUserReceives.isEmpty(),"");
    }
}