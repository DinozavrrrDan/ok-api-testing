package com.nedorezov.tests.group.getCounters.errors;

import com.nedorezov.core.config.Config;
import com.nedorezov.core.enums.CounterTypes;
import com.nedorezov.core.requesters.GetGroupCounterRequester;
import com.nedorezov.core.responses.ErrorResponse;
import com.nedorezov.tests.base.BaseTest;
import lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GetCountersCheckErrorTest extends BaseTest {

    @Builder
    static class TestParams {
        String description;
        String error_msg;
        Long error_code;
        Map<String, String> counterType;
        Map<String, String> queryParams;

        @Override
        public String toString() {
            return description;
        }
    }

    private final static Long notExistGroupId = 0L;
    private final static Long existGroupId = 0L;
    private final static String notExistCounterTypes = "NOT_EXIST";

    private static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of(new TestParams
                        .TestParamsBuilder()
                        .description("CASE_1. Проверка с несуществующим group_id")
                        .error_msg("NOT_FOUND : not.found.object")
                        .error_code(300L)
                        .queryParams(Map.of(
                                "group_id", notExistGroupId.toString(),
                                "counterTypes", CounterTypes.PHOTOS.getValue()))
                        .build()
                ),
                Arguments.of(new TestParams
                        .TestParamsBuilder()
                        .description("CASE_2. Проверка с несуществующим counterType")
                        .error_msg("PARAM : Invalid parameter counterTypes value  : [" + notExistCounterTypes + "]")
                        .error_code(100L)
                        .queryParams(Map.of(
                                "group_id", existGroupId.toString(),
                                "counterTypes", notExistCounterTypes))
                        .build()
                ),
                Arguments.of(new TestParams
                        .TestParamsBuilder()
                        .description("CASE_3. Не передан обязательный параметр group_id")
                        .error_msg("PARAM : Missing required parameter group_id")
                        .error_code(100L)
                        .queryParams(Map.of(
                                "counterTypes", CounterTypes.PHOTOS.getValue()))
                        .build()
                ),
                Arguments.of(new TestParams
                        .TestParamsBuilder()
                        .description("CASE_4. Не передан обязательный параметр counterType")
                        .error_msg("PARAM : Missing required parameter counterTypes")
                        .error_code(100L)
                        .queryParams(Map.of(
                                "group_id", existGroupId.toString()))
                        .build()
                ),
                Arguments.of(new TestParams
                        .TestParamsBuilder()
                        .description("CASE_5. Передан counterType равный null")
                        .error_msg("PARAM : Invalid parameter counterTypes value  : [null]")
                        .error_code(100L)
                        .queryParams(Map.of(
                                "group_id", existGroupId.toString(),
                                "counterTypes", "null"))
                        .build()
                ),
                Arguments.of(new TestParams
                        .TestParamsBuilder()
                        .description("CASE_5. Передан group_id равный null")
                        .error_msg("PARAM_GROUP_ID : Invalid group_id [null]")
                        .error_code(160L)
                        .queryParams(Map.of(
                                "group_id", "null",
                                "counterTypes", CounterTypes.PHOTOS.getValue()))
                        .build()
                )
        );
    }

    @Tag("group.getCounters")
    @ParameterizedTest(name = "{0}")
    @MethodSource("dataProvider")
    @DisplayName("Проверка ошибок")
    void getCountersCheckErrorTest(TestParams params) {
        var response = new GetGroupCounterRequester(
                config,
                params.queryParams
        ).sendRequest().body().as(ErrorResponse.class);

        assertAll("Валидация тела ошибки",
                () -> assertThat(response.getError_code())
                        .as("error_code не совпал с ожидаемым")
                        .isEqualTo(params.error_code),
                () -> assertThat(response.getError_msg())
                        .as("error_msg не совпал с ожидаемым")
                        .isEqualTo(params.error_msg)

        );
    }

}
