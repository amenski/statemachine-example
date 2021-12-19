package com.workmotion.ems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Supplier;

import com.workmotion.ems.swagger.model.ResponseBase;
import com.workmotion.ems.util.Constants;
import com.workmotion.ems.util.EMSException;

public class AssertTestHelper {

    public static <T extends ResponseBase> void checkForSuccess(T response) {
        assertTrue(response.isSuccess());
        assertEquals(Constants.SUCCESS, response.getResultCode().intValue());
        assertThat(response.getErrors()).isNull();
        assertThat(response.getMessage()).isNull();
    }

    public static <T extends ResponseBase> void checkForGenericException(T response) {
        assertFalse(response.isSuccess());
        assertNotEquals(Constants.SUCCESS, response.getResultCode().intValue());
        assertThat(response.getMessage()).isNotNull();
    }

    public static <T extends ResponseBase> void checkForEMSException(T response, Supplier<EMSException> ex) {
        assertFalse(response.isSuccess());
        assertEquals(ex.get().getInternalCode(), response.getResultCode().intValue());
        assertThat(response.getMessage()).isNotNull();
    }

    private AssertTestHelper() {
        throw new IllegalStateException("Utility Class");
    }
}
