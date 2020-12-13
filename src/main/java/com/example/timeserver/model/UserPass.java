package com.example.timeserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel()
public class UserPass {
    @ApiModelProperty()
    private String username;
    @ApiModelProperty()
    private String password;
    @JsonIgnore

    private String somethingNotInJson;
}
