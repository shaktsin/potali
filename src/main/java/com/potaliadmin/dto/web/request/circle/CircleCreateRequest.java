package com.potaliadmin.dto.web.request.circle;

import com.potaliadmin.constants.circle.CircleType;
import com.potaliadmin.dto.web.request.framework.GenericRequest;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shakti on 28/1/15.
 */
public class CircleCreateRequest extends GenericRequest {

    private String name;
    private boolean moderate;
    private Integer circleId;
    private String desc;

    @Override
    public boolean validate() {
        boolean valid = super.validate();

        if (valid && StringUtils.isBlank(name)) {
            valid = false;
        }
        if (valid && circleId == null) {
            valid = false;
        }
        if (valid && !CircleType.validCircle(circleId)) {
            valid = false;
        }
        return valid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isModerate() {
        return moderate;
    }

    public void setModerate(boolean moderate) {
        this.moderate = moderate;
    }

    public Integer getCircleId() {
        return circleId;
    }

    public void setCircleId(Integer circleId) {
        this.circleId = circleId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
