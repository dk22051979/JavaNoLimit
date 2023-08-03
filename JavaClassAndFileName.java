public class JavaClassAndFileName
{
  private String className;
  private String fileName;

  public JavaClassAndFileName(String strClassName, String strFileName)
  {
    this.className = strClassName;
    this.fileName = strFileName;
  }

  public String getClassName() {
         return className;
  }

  public String getFileName() {
         return fileName;
  }
  public String toString(){
         return className;
  }
}


