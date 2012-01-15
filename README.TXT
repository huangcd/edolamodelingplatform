1、   项目运行环境（Eclipse3.7 Indigo，其它沒有尝试过。。。），需要有EMF、GEF等插件（具体请根据错误提示安装），另外需要导入libProject中的项目
   1）辅助插件安装（windowBuilder（安装地址：http://download.eclipse.org/windowbuilder/WB/release/R201109201200/3.7/）：可能会有依赖项不能满足，需要按照提示从别的机器拷贝，主要有org.eclipse.wst.sse.*及org.apache.xml*）
2、   为保证代码风格一致，在编辑项目之前请导入.settings/eclipse-java-google-style.xml。
        导入方法：Eclipse->window->preferences->Java->Code Style->Formatter->Import 选择eclipse-java-google-style.xml文件。
      重要：提交代码之前对于修改过的文件，请先在Eclipse里面执行格式化代码操作（默认快捷键是Ctrl+Shift+F），以保持代码风格一致。
      
3、  将输出信息写到Console中去：
        MessageConsole console = new MessageConsole("信息", null);      // 信息改成需要的名字  
        ConsolePlugin.getDefault().getConsoleManager()
                        .addConsoles(new IConsole[] {console});
        try {
            console.activate();
            IOConsoleOutputStream out = console.newOutputStream();      // 获得Console的输出流
            out.setColor(ColorConstants.red);                           // 设置颜色
            out.setFontStyle(SWT.BOLD);                                 // 设置字体
            out.write("hello world");
        } catch (IOException e) {
            e.printStackTrace();
        }

4、	 如果运行是出现莫名其妙的错误，请尝试如下步骤：
	 1）删除eclipse目录下的runtime-EdolaModelingPlatform.product
	 2）删除eclipse里面run configure...相关项
	 3）将files目录拷贝到eclipse目录下
	 
5、请将data\tool\file文件夹放到eclipse的安装目录下。

6、问题：项目视图，若如果提示.project文件不存在。
   解决：将temp下的.project文件，复制到项目的上一层文件夹。
   原因：实现项目视图时，使用eclipse的Iproject类，需读取.project文件。
         但是和edola里的项目文件.project重名了。。。
   暂时将项目视图需要的.project放到上一层，
   待edola建模时的.project后缀名修改后，则放在同一个项目目录下。
         