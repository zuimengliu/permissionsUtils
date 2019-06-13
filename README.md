# permissionsUtils
权限及dialog工具类
好用的权限工具类，并且做了多种机型的打开权限页面的适配，还包含了好用的dialog工具类
>  Step 1.先在 build.gradle(Project:XXXX) 的 repositories 添加:

    allprojects {
    	repositories {
    		...
    		maven { url "https://jitpack.io" }
    	}
    }

> Step 2. 然后在 build.gradle(Module:app) 的 dependencies 添加:

    dependencies { 
    	compile 'com.github.zuimengliu.permissionsUtils:dialoglibrary:1.2' compile'com.github.zuimengliu.permissionsUtils:permissionlibrary:1.2' 
    }

    dependencies {
       compile 'com.github.zuimengliu.permissionsUtils:dialoglibrary:1.2'
       compile 'com.github.zuimengliu.permissionsUtils:permissionlibrary:1.2'
    }
