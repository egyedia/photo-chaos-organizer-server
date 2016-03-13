package com.dubylon.photochaos.app;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CopyDatedFolderTaskConfig.class, name = "copyDatedFolderTaskConfig")
})
public abstract class TaskConfig {
}
