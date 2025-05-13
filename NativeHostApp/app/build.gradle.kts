plugins {
    alias(libs.plugins.android.application)
    // 如果你的原生 app 代码中也使用了 Kotlin，保留下面这行
    // alias(libs.plugins.kotlin.android)
    // 如果你的原生 app UI 中使用了 Jetpack Compose，保留下面这行
    // alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.ritchey.nativehostapp" // 这个是你新建项目时设置的，保持不变
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ritchey.nativehostapp" // 这个是你新建项目时设置的，保持不变
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // --- 添加 NDK ABI Filters ---
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }
        // --- 结束 NDK ABI Filters ---
    }

    buildTypes {
        release {
            isMinifyEnabled = false // 正式发布时建议设为 true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // --- 修改 Java 版本以匹配 unityLibrary (如果它用的是Java 17) ---
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // --- 结束 Java 版本修改 ---
    }
    // 如果你确实在 app 模块中使用了 Kotlin，你可能还需要这个：
    // kotlinOptions {
    //    jvmTarget = "17"
    // }

    // --- 添加 Packaging Options ---
    packagingOptions {
        pickFirst("lib/armeabi-v7a/libmain.so")
        pickFirst("lib/arm64-v8a/libmain.so")
        pickFirst("lib/armeabi-v7a/libunity.so")
        pickFirst("lib/arm64-v8a/libunity.so")
        pickFirst("lib/armeabi-v7a/libc++_shared.so")
        pickFirst("lib/arm64-v8a/libc++_shared.so")
        // 如果构建时报告其他 .so 文件冲突，也按照这个格式添加进来
    }
    // --- 结束 Packaging Options ---

    // buildFeatures 块，如果你的原生 UI 用到了 Compose 或 ViewBinding，可以保留
    // 如果你的原生部分非常简单，甚至可以先注释掉 buildFeatures 块来排除干扰
    buildFeatures {
        // compose = true // 如果原生部分不用Compose，可以设为false或删除这行
        // viewBinding = true // 如果原生部分不用ViewBinding，可以设为false或删除这行
    }
}

dependencies {
    // --- 添加对 Unity Library 模块的依赖 ---
    implementation(project(":unityLibrary"))
    // --- 结束 Unity Library 模块的依赖 ---

    // 你现有的原生依赖项
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity) // 对于 AppCompatActivity 是必需的
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // 下面这些是 Jetpack Compose 相关的依赖，如果你的原生部分
    // (MainActivity, UnityPlayerActivity的宿主等) 不使用 Compose UI，
    // 理论上可以不加或者注释掉，以简化项目。
    // 但如果你的项目模板默认生成了它们，暂时保留也无大碍。
    // implementation(libs.lifecycle.runtime.ktx)
    // implementation(libs.activity.compose)
    // implementation(platform(libs.compose.bom))
    // implementation(libs.ui)
    // implementation(libs.ui.graphics)
    // implementation(libs.ui.tooling.preview)
    // implementation(libs.material3)
    // implementation(libs.navigation.fragment)
    // implementation(libs.navigation.ui)
    // androidTestImplementation(platform(libs.compose.bom))
    // androidTestImplementation(libs.ui.test.junit4)
    // debugImplementation(libs.ui.tooling)
    // debugImplementation(libs.ui.test.manifest)
}