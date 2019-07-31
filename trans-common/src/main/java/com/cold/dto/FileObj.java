package com.cold.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Auther: ohj
 * @Date: 2019/7/25 10:30
 * @Description:
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileObj {
    private String filename;
    private String originalFileName;
}