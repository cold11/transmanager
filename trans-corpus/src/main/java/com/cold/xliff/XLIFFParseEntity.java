package com.cold.xliff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.sf.okapi.common.Event;
import net.sf.okapi.common.filterwriter.IFilterWriter;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/8/23 13:26
 * @Description:
 */
@Getter
@Setter
@AllArgsConstructor
public class XLIFFParseEntity {
    private List<TransUnit> transUnits;
    private List<Event> eventList;
    private IFilterWriter filterWriter;
}