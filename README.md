# CySwitch    ![CySwitch](https://jitpack.io/v/ausboyue/CySwitch.svg)
A custom switch view.

## ScreenShot
![easytransition](https://github.com/ausboyue/CySwitch/blob/master/art/cy_switch.gif) 

## Download from Gradle

Add to your root build.gradle:
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

Add the dependency:
```groovy
    dependencies {
            implementation 'com.github.ausboyue:CySwitch:1.0.2'
    }
```

## Friendly Hints
**If you can't download it, maybe you have used the Google's repository that you can't connect to the Google server.Please open the proxy or top the target repository.As follows：**
```groovy
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' } // target repository,be top
            jcenter()
            google() // Google's repository
        }
    }
```

## Download from Maven

Add the JitPack repository to your build file:
```groovy
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
```

Add the dependency:
```groovy
    <dependency>
        <groupId>com.github.ausboyue</groupId>
        <artifactId>CySwitch</artifactId>
        <version>1.0.2</version>
    </dependency>
```

# Get Started

**1. Edit codes in layout xml file as below :**

```xml
    <cn.icheny.view.CySwitch
        android:id="@+id/cy_switch"
        android:layout_width="120dp"
        android:layout_height="50dp"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="25dp"
        app:borderColorChecked="@android:color/holo_green_dark"
        app:borderColorUnchecked="@android:color/holo_blue_light"
        app:borderWidth="0dp"
        app:isChecked="true"
        app:sliderHeight="40dp"
        app:sliderRadius="0dp"
        app:sliderWidth="40dp"
        app:viewRadius="0dp" />
```

**2. Edit codes in java file as below :**
```java
        CySwitch cy_switch = findViewById(R.id.cy_switch);
        cy_switch.setViewRadius(radius);
        cy_switch.setSliderRadius(radius);
        cy_switch.setBorderWidth(width);
        cy_switch.checkable(true);
        cy_switch.setOnCheckedChangeListener(new CySwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CySwitch switchView, boolean isChecked) {
                Toast.makeText(MainActivity.this, "Switch " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });
```

# Bugs Report

If you find any bug when using it, please contact [me](mailto:ausboyue@qq.com). Thanks for helping me making better.

# Author

Cheny - @[ausboyue on GitHub](https://github.com/ausboyue/), @[www.icheny.cn](http://www.icheny.cn)

# Other

Please give me some time to update the documentation.

# Release note

## 1.0.1
 - Optimize checked status of the CySwitch when it be toggled.

## 1.0.0
 - release first version v1.0.0 
 - nothing now
