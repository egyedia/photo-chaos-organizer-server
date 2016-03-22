package com.dubylon.photochaos.task.copytodatedfolderbymeta;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.task.IPcoTask;
import com.dubylon.photochaos.task.PcoTaskTemplate;
import com.dubylon.photochaos.task.PcoTaskTemplateParameter;
import com.dubylon.photochaos.task.TaskTemplateParameterCopyOrMove;
import com.dubylon.photochaos.task.copytodatedfolderbyname.CopyToDatedFolderGetData;

@PcoTaskTemplate(languageKeyPrefix = "task.copyFilesByDateFromFileMeta.")
public class CopyFilesToFoldersByCaptionDateFromFileMetaTask implements IPcoTask {

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.FOLDER,
      mandatory = true,
      defaultValue = ""
  )
  private String sourceFolder;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.FOLDER,
      mandatory = true,
      defaultValue = ""
  )
  private String destinationFolder;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.FOLDERSUFFIX,
      mandatory = true,
      defaultValue = ""
  )
  private String suffix;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.SHORTDATEFORMAT,
      mandatory = true,
      defaultValue = "YYYYMMDD"
  )
  private String newFolderDateFormat;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.COPYORMOVE,
      mandatory = true,
      defaultValue = "copy"
  )
  private TaskTemplateParameterCopyOrMove fileOperation;

  public CopyFilesToFoldersByCaptionDateFromFileMetaTask(CopyToDatedFolderGetData response, boolean performOperations) {

  }

}
