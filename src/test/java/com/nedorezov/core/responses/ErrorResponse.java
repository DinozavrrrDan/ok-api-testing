
package com.nedorezov.core.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String error_msg;
    private long error_code;
    private long error_data;

}
