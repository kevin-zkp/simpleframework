package org.simpleframework.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPathInfo {
    private String httpMethod;
    private String httpPath;
}
