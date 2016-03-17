package com.dubylon.photochaos.task.copytodatedfolder;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplate;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameter;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;

import java.util.List;

public class CopyToDatedFolderByFileNameTaskTemplate extends TaskTemplate {

  public CopyToDatedFolderByFileNameTaskTemplate() {
    super();
    this.name = "Copy to dated folder by date in file name";
    this.description = "The task will search all the files under the source folder (subfolders including).\n" +
        "It will then try to extract the date from the filename.\n" +
        "For each day that was found in one of the filenames, a new folder under the target folder will be created.\n" +
        "Each file will be copied to the corresponding new folder.\n" +
        "The name format for the new folders can be set, and you can specify if the files should be copied or moved" +
        ".\n" +
        "A prefix can be specified, which will be added into the name of each of the newly created folders (to " +
        "represent the source of the pictured..)";
    this.type = "CopyToDatedFolderByFileNameTaskTemplate";
    List<TaskTemplateParameter> parameters = this.parameters.getParameters();

    TaskTemplateParameter sourceFolder = new TaskTemplateParameter();
    sourceFolder.setName("Source folder");
    sourceFolder.setDescription("The folder will be explored recursively, all the images will be collected");
    sourceFolder.setMandatory(true);
    sourceFolder.setType(TaskTemplateParameterType.FOLDER);
    parameters.add(sourceFolder);

    TaskTemplateParameter destinationFolder = new TaskTemplateParameter();
    destinationFolder.setName("Destination folder");
    destinationFolder.setDescription("New folders will be created under this location.");
    destinationFolder.setMandatory(true);
    destinationFolder.setType(TaskTemplateParameterType.FOLDER);
    parameters.add(destinationFolder);

    TaskTemplateParameter imageSource = new TaskTemplateParameter();
    destinationFolder.setName("New folder name suffix");
    destinationFolder.setDescription("A suffix string that will be added after the newly created folder names.");
    destinationFolder.setMandatory(true);
    destinationFolder.setType(TaskTemplateParameterType.SUFFIX);
    parameters.add(destinationFolder);

    TaskTemplateParameter newFolderDateFormat = new TaskTemplateParameter();
    newFolderDateFormat.setName("New folder name suffix");
    newFolderDateFormat.setDescription("A suffix string that will be added after the newly created folder names.");
    newFolderDateFormat.setMandatory(true);
    newFolderDateFormat.setType(TaskTemplateParameterType.SUFFIX);
    parameters.add(destinationFolder);
  }
}
