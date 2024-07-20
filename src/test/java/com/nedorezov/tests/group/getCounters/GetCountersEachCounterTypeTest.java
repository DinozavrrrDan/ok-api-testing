package com.nedorezov.tests.group.getCounters;

import com.nedorezov.core.responses.getCountersTestResponse.GetCountersTestResponse;
import com.nedorezov.core.enums.CounterTypes;
import com.nedorezov.core.requesters.GetGroupCounterRequester;
import com.nedorezov.tests.base.BaseTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GetCountersEachCounterTypeTest extends BaseTest {

    private static final Long existingGroupId = 53821101179054L;

    @Tag("group.getCounters")
    @ParameterizedTest(name = "{0}")
    @EnumSource(CounterTypes.class)
    @DisplayName("Проверка каждого из возможных параметров counterTypes")
    void getCountersCheckErrorTest(CounterTypes counterType) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("group_id", existingGroupId.toString());
        queryParams.put("counterTypes", counterType.getValue());
        var response = new GetGroupCounterRequester(
                config,
                queryParams
        ).sendRequest();

        response.body().as(GetCountersTestResponse.class);

        assertThat(response.statusCode())
                .as("StatusCode не совпал с ожидаемым")
                .isEqualTo(HttpStatus.SC_OK);
    }
}
