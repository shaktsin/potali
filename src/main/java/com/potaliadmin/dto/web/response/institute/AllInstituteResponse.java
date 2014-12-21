package com.potaliadmin.dto.web.response.institute;

import com.potaliadmin.constants.json.DtoJsonConstants;
import com.potaliadmin.dto.internal.cache.institute.InstituteVO;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

import java.util.List;

/**
 * Created by Shakti Singh on 12/15/14.
 */
public class AllInstituteResponse extends GenericBaseResponse {

  List<InstituteDto> instituteDtoList;

  public List<InstituteDto> getInstituteDtoList() {
    return instituteDtoList;
  }

  public void setInstituteDtoList(List<InstituteDto> instituteDtoList) {
    this.instituteDtoList = instituteDtoList;
  }

  /*@Override
  protected List<String> getKeys() {
    List<String> keys = super.getKeys();
    keys.add(DtoJsonConstants.ALL_INST);
    return keys;
  }

  @Override
  protected List<Object> getValues() {
    List<Object> values = super.getValues();
    values.add(this.instituteDtoList);
    return values;
  }*/
}
