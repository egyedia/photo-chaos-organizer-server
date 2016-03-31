package com.dubylon.photochaos.task.copytodatedfolderbymeta;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.rest.task.TaskPreviewOrRunGetData;
import com.dubylon.photochaos.task.IPcoTask;
import com.dubylon.photochaos.task.PcoTaskTemplate;
import com.dubylon.photochaos.task.PcoTaskTemplateParameter;
import com.dubylon.photochaos.task.TaskTemplateParameterCopyOrMove;

@PcoTaskTemplate(languageKeyPrefix = "task.copyFilesByDateFromFileMeta.")
public class CopyFilesToFoldersByCaptionDateFromFileMetaTask implements IPcoTask {

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.PATH,
      mandatory = true,
      defaultValue = "",
      order = 1
  )
  private String sourceFolder;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.PATH,
      mandatory = true,
      defaultValue = "",
      order = 2
  )
  private String destinationFolder;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.FOLDERSUFFIX,
      mandatory = false,
      defaultValue = "",
      order = 3
  )
  private String newFolderSuffix;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.SHORTDATEFORMAT,
      mandatory = true,
      defaultValue = "yyyyMMdd",
      order = 4
  )
  private String newFolderDateFormat;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.COPYORMOVE,
      mandatory = true,
      defaultValue = "copy",
      order = 5
  )
  private TaskTemplateParameterCopyOrMove fileOperation;

  public CopyFilesToFoldersByCaptionDateFromFileMetaTask() {

  }

  @Override
  public void execute(TaskPreviewOrRunGetData response, boolean performOperations) {

  }
}
