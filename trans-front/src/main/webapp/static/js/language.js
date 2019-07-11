var language={
    init:function (basePath) {
        var languages = [];
        var sourceLanguage = $("#sourceLan");
        sourceLanguage.empty();
        $.ajaxSettings.async = false;
        $.getJSON(basePath+'selectLanguage',function(json){
            if(json.success){
                languages = json.msg;
            }
        });
        $.ajaxSettings.async = true;
        var targetLanguage = $("#targetLan");
        $("<option value=''>==请选择源语言==</option>").appendTo(sourceLanguage);
        languages.forEach(function (value, index) {
            $("<option value='" + value.languageCode + "'>" + value.languageName + "</option>").appendTo(sourceLanguage);
        });
        sourceLanguage.change(function(){
            targetLanguage.empty();
            var parent=sourceLanguage.val();
            if(parent==''){
                $("<option value=''>==请选择目标语言==</option>").appendTo(targetLanguage);
                return;
            }
            var filterarray = $.grep(languages,function(value){
                return value.languageCode!=parent;
            });
            for(var i=0;i<filterarray.length;i++){
                if((parent=='en'&&filterarray[i].languageCode=='jp')||(parent=='en'&&filterarray[i].languageCode=='ko'))continue;
                if((parent=='jp'&&filterarray[i].languageCode=='en')||(parent=='jp'&&filterarray[i].languageCode=='ko'))continue;
                if((parent=='ko'&&filterarray[i].languageCode=='en')||(parent=='ko'&&filterarray[i].languageCode=='jp'))continue;
                $("<option value='" +filterarray[i].languageCode + "'>" + filterarray[i].languageName + "</option>").appendTo(targetLanguage);
            }
        });
    }
}