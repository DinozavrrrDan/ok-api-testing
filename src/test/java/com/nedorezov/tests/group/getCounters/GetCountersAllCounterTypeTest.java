package com.nedorezov.tests.group.getCounters;

import com.nedorezov.core.responses.getCountersTestResponse.GetCountersTestResponse;
import com.nedorezov.core.enums.CounterTypes;
import com.nedorezov.core.requesters.GetGroupCounterRequester;
import com.nedorezov.tests.base.BaseTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GetCountersAllCounterTypeTest extends BaseTest {
    private static final Long existingGroupId = 53821101179054L;

    @Test
    @Tag("group.getCounters")
    @DisplayName("CASE_1 Проверка каждого из возможных параметров counterTypes")
    void getCountersCheckErrorTest() {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("group_id", existingGroupId.toString());
        String counterTypes = Arrays.stream(CounterTypes.values())
                .map(Enum::toString)
                .collect(Collectors.joining(","));
        queryParams.put("counterTypes", counterTypes);
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
