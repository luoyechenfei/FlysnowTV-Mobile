# 飞雪TV 手机版（FlysnowTV-Mobile）· Android 手机 / 平板直播播放器

主流深蓝主调、玻璃拟态海报墙风格的**移动端**电视直播播放器。原生 Android（Kotlin + Media3/ExoPlayer），装在你的**手机或平板**上自用测试，支持横竖屏自适应与完整触摸手势。

> **本仓库是手机版，与电视版分开维护**
>
> | 版本 | 仓库 | 包名 | 适用设备 | APK |
> | --- | --- | --- | --- | --- |
> | **手机版（本仓库）** | [`luoyechenfei/FlysnowTV-Mobile`](https://github.com/luoyechenfei/FlysnowTV-Mobile) | `com.iptv.tvplayer.mobile` | 手机 / 平板 | `FlysnowTV-Mobile.apk` |
> | 电视版 | [`luoyechenfei/FlysnowTV`](https://github.com/luoyechenfei/FlysnowTV) | `com.iptv.tvplayer` | Android TV 盒子 / 电视 | `FlysnowTV.apk` |
>
> 两者源码、Release、APK 完全独立，**不放在同一个仓库**。包名不同、可在一台设备上共存；使用同一张证书签名（见「与电视版的关系」）。

## 主要特色

与电视版「纯遥控器 + 三行分页」的定位不同，手机版专注「**触屏 + 手机/平板自适应 + 常规播放器体验**」：

| 特色 | 说明 |
| --- | --- |
| **手机 / 平板自适应** | 横屏、竖屏自动切换布局；频道墙列数按屏幕宽度动态计算（竖屏约 2 列、横屏更多），卡片宽高比随列宽自适应，不再固定 3 行、不再分页——整个分类一次连续上下滑动浏览到底 |
| **完整触摸手势** | 播放画面支持单击显隐控制层、双击播放/暂停、左右滑动换台、上下滑动调音量（右半屏）与亮度（左半屏），带居中提示；卡片长按收藏 |
| **常规播放器控制条** | 播放时顶部为「返回 / 频道名 + 线路 / 设置」，底部为「上一台 / 播放暂停 / 下一台」与当前节目、时间进度条；符合手机播放器的使用直觉 |
| **顶部横滑分类** | 全部分类 HorizontalScrollView 横向排列，点选高亮并自动滚动到可见；不再有「更多」弹窗与翻页按钮 |
| **顶部菜单悬浮、不挤动频道** | 滑动时顶部分类 / 设置 / 台标随手势淡出扩大视野，停手立即恢复；菜单以浮层覆盖在频道之上，下面的频道卡保持原位、不被推挤下移 |
| **台标兜底显示** | 订阅无 `tvg-logo` 或台标加载失败时，自动用频道名生成文字台标（圆角深蓝底 + 蓝色描边），不再只显示一个首字母 |
| **零内置、零分发** | 不内置任何频道源、测试流或示例订阅，没有启动广告与运营内容；频道完全来自你自己添加的订阅，纯净自用 |
| **播得稳** | 继承电视版全部播放稳定性：多线路自动切换与失败重试、卡顿分级（先降清晰度再换源）、网速自适应画质、杜比 / DTS 无声音轨自动换源、HLS 直播窗口越界恢复；可交给 VLC / MX / Kodi 外核 |
| **IPv6 优先** | 播放链路使用 OkHttp + 自定义 `Ipv6PreferredDns`，有 IPv6 优先走 IPv6、连不通自动回退 IPv4；纯 IPv4 网络保持系统顺序 |
| **冷启动续播** | 进入软件即直接全屏播放上次退出时正在播放的频道（用稳定 key 记录，换源 / 顺序变化不丢） |
| **配置零门槛** | 设置里粘贴订阅链接一键导入；也可用扫码 / 局域网网页配置订阅、请求头、Token，不用在小屏上逐字输入 |
| **节目单完整** | XMLTV EPG：时间轴网格、当前 / 下一节目、节目预约提醒、按节目回看（需源支持） |
| **每页有返回** | 设置、节目单、二维码配置等所有页面 / 浮层均有明确的返回入口，触屏操作无死角 |
| **可维护** | App 内展示版本信息、日志反馈（扫码 / 微信 / QQ 分享）；Release 页提供签名安装包 |

> 当前版本 **1.0.0-mobile（versionCode 1）**，包名 `com.iptv.tvplayer.mobile`，minSdk 21 / targetSdk 34。
> 本仓库只包含源码：编译产物与 release 签名密钥按 `.gitignore` 排除，请按「重新打包」一节自行构建，或从下方 Release 下载现成签名包。
> 直接装现成签名包：[Releases 发布页](https://github.com/luoyechenfei/FlysnowTV-Mobile/releases)（最新为 **v1.0.0-mobile**）。
>
> **本仓库与 APK 均不收录、不内置任何频道源**：没有 m3u/m3u8 播放列表、没有示例订阅、没有测试流、没有内置央视频道。所有频道都由使用者自行导入订阅；文中 `https://www.example.com/...` 只是格式占位，不是可用地址。

## 版本更新记录

| 项目 | 说明 |
| --- | --- |
| 当前版本 | **1.0.0-mobile（versionCode 1）** · 首个手机版独立发行：手机 / 平板横竖屏自适应、完整触摸手势、常规播放器控制条、顶部横滑分类、取消翻页、台标兜底、顶部菜单浮层化；并继承电视版 1.9.10 的 IPv6 优先播放与冷启动续播 |
| 最新签名包 | [v1.0.0-mobile Release](https://github.com/luoyechenfei/FlysnowTV-Mobile/releases/tag/v1.0.0-mobile) · `FlysnowTV-Mobile.apk`（约 6.7 MB，V1 + V2 签名） |
| 与电视版关系 | 独立仓库、独立包名、独立 Release；源码基线来自电视版 1.9.10，针对手机 / 平板重做导航与交互 |
| 仓库内容 | 仅源码与资源，**不含任何频道源 / 测试订阅 / 示例播放列表 / 内置频道**；频道全部由使用者自行导入 |

### 1.0.0-mobile（versionCode 1）· 首个手机版发行

从电视版 1.9.10 派生为独立子项目，按手机 / 平板使用场景系统性改造：

| 项目 | 更新内容 |
| --- | --- |
| **独立子项目** | 独立仓库 `FlysnowTV-Mobile`、独立包名 `com.iptv.tvplayer.mobile`（可与电视版同机共存）、独立产物 `FlysnowTV-Mobile.apk`；移除 LEANBACK_LAUNCHER、声明 `touchscreenRequired`，FileProvider authority 用 `${applicationId}`，与电视版代码库互不覆盖 |
| **横竖屏自适应** | 列数与卡片尺寸按可用宽高动态计算，竖屏 2 列、横屏更多；设置页 / 标题栏 / 控制条按屏宽切换 padding 与两行布局，窄屏不再变形 |
| **触摸手势（核心）** | 新增 `TouchGestures.kt`：单击显隐浮层、双击播放/暂停、水平滑动换台（左右）、竖直滑动调音量 / 亮度（右音量 / 左亮度，带居中 Toast）；仅在播放模式启用，墙面交给 RecyclerView |
| **常规播放器控制条（核心）** | 新增 `playerControls` 覆盖层：顶部「‹ 返回 / 频道名 + 线路 / ⚙ 设置」，底部「⏮ 上一台 / ⏸ 播放暂停 / ⏭ 下一台」+ 当前节目 + 时间进度；原电视版信息卡 / 底部栏逻辑全部转驱动新控制条，五个按钮点击与手势互不冲突 |
| **顶部横滑分类 + 取消翻页** | `GroupAdapter` 重写为纯横滑标签条，全部分类一字排开、点选高亮；提交全部频道、整列表连续滑动，删除翻页按钮与「上下滑动浏览」提示 |
| **顶部菜单浮层化（核心）** | 频道墙容器改 `FrameLayout`：频道网格铺满且位置恒定，顶部栏 / 分类 / 提示作为浮层覆盖其上。滑动时浮层淡出扩大视野、停手立即恢复；菜单弹出只遮盖下方频道，不推动网格下移；上滑卡片绘制进 padding 区被浮层遮住而非留白 |
| **台标兜底显示** | 新增 `TextLogoDrawable.kt`：订阅无台标或加载失败时用频道名生成圆角文字台标，替代首字母；`LogoFitView` 等比缩放完整显示，不裁剪 |
| **返回入口齐全** | 设置、EPG 时间轴、二维码配置等子页各加返回按钮；EPG 面板、数字选台卡等浮层均有取消 / 关闭入口 |
| **继承电视版能力** | IPv6 优先播放（`Ipv6PreferredDns` + OkHttp 数据源）、冷启动直接播放上次频道、多线路自动切换、卡顿分级处理、网速自适应画质、起播提速、聚合去重、订阅管理、XMLTV 节目单、扫码配置、日志反馈 |

## 与电视版的关系

- **两套独立仓库、独立源码、独立 Release**：本仓库是手机版 `FlysnowTV-Mobile`，电视版在 `FlysnowTV`，互不覆盖。
- **包名不同可共存**：手机版 `com.iptv.tvplayer.mobile`，电视版 `com.iptv.tvplayer`，可同时装在同一台手机 / 平板上，数据互不干扰。
- **同证书签名**：两者共用 `CN=FeiXueTV` 证书（SHA-256 `22bf6bf2…`），从本仓库 Release 安装的手机版可直接覆盖同包名的旧手机版。
- **源码基线**：手机版从电视版 1.9.10 复制派生，保留了订阅解析、播放核心、EPG、扫码配置等通用能力，导航与交互按触屏重做。因此设置里部分「关于 / 检测更新」文案仍标注电视版历史版本号，属正常沿用；**手机版的版本以本仓库 Release 的 `v1.0.0-mobile` 为准**。

## 快速开始

1. 从 [Releases 发布页](https://github.com/luoyechenfei/FlysnowTV-Mobile/releases) 下载 `FlysnowTV-Mobile.apk`。
2. 传到手机 / 平板点击安装；如被拦截，在「设置 → 安全」里开启「允许安装未知来源应用」。也可 `adb install -r FlysnowTV-Mobile.apk`。
3. 首次打开是空的频道墙；进入「设置」粘贴一个你自己的聚合订阅链接即可开始（**应用不内置任何频道**）。
4. 也支持导入 m3u 文本或第三方 JSON 接口，详见「订阅与播放源」。

## 操作指南

### 触摸手势（播放画面）

| 手势 | 作用 |
| --- | --- |
| 单击画面 | 显示 / 隐藏顶部与底部控制条 |
| 双击画面 | 播放 / 暂停 |
| 左右滑动 | 上一个 / 下一个频道 |
| 右半屏上下滑动 | 调节音量（上滑增大、下滑减小） |
| 左半屏上下滑动 | 调节屏幕亮度（上滑增亮、下滑变暗） |
| 长按频道卡 | 收藏 / 取消收藏该频道 |

### 播放器控制条

- **顶部**：`‹` 返回频道墙；中间为当前频道名与线路；`⚙` 打开功能菜单（收藏 / 节目单 / 线路 / 画质 / 内核 / 设置 / 清理缓存等）。
- **底部**：`⏮` 上一台、`⏸/▶` 播放暂停、`⏭` 下一台；上方显示当前节目与播放进度。
- 信息浮层会在静止后自动收起，单击画面可随时唤回。

### 频道墙

- 顶部横滑分类条：点选分类高亮，滑动查看更多分类；「★ 我的收藏」「⏱ 最近观看」为虚拟分类。
- 上下连续滑动浏览当前分类全部频道，**不再分页**。
- 滑动过程中顶部栏淡出、停手立即恢复；顶部菜单为浮层，弹出时不推动频道下移。
- 冷启动会直接播放上次退出时的频道；点 `‹` 返回频道墙。

## 订阅与播放源

本应用**不内置、不分发任何频道或直播流地址**。所有频道都来自使用者自行添加的订阅源（聚合 m3u / txt、第三方 JSON 接口、单个流地址等）。点卡片即用 ExoPlayer 直连播放，不跳转任何官方网页。

- 请只导入你有权使用的源；自用测试请保持私用，不要分发、不要公开提供流地址。
- 频道按 m3u 里的 `group-title` 自动分组；没有分组的归入「其他」。
- 同一频道在多个源里出现时自动合并成多线路，主线路来自排在最前的启用订阅，其余为备用源；播放失败自动切换并记忆可用线路。
- 分组名写法差异（`卫视` / `卫视频道`）自动归并，避免筛选条出现重复项。

### 订阅管理

「设置 → 第三方接口」里可添加 / 编辑 / 停用 / 排序 / 校验 / 删除订阅，并可整体导出 / 导入订阅备份（换机、分享、留底）。「一键导入订阅链接」支持粘贴一条或多条 `.m3u` / `.txt` 地址、可选请求头，逐条校验频道数。

### 第三方接口：GET / POST / Token

| 字段 | 说明 |
| --- | --- |
| 接口地址 | m3u / m3u8 / JSON 接口 / 单个流地址，支持 `{token}` 占位 |
| 请求方式 | `GET` 或 `POST`（填写其它值自动回退 GET） |
| POST 请求体 | 支持 `{token}`；以 `{` 开头自动用 `application/json`，否则表单格式 |
| 自定义请求头 | 每行 `Key: Value`，值里可写 `{token}`；接口请求与视频播放都会带上 |
| JSON 字段映射 | `name=title,url=playUrl,logo=icon,group=category,tvgId=channelId,root=data` |
| Token 接口地址 | 留空表示不需要 Token；GET/POST 跟随该源的请求方式与请求体 |
| Token 字段路径 | 如 `data.token`、`result.0.access_token`；留空 = 整段响应当 Token |
| Token 刷新周期 | 分钟，0 = 跟随全局设置 |

**自动识别的返回格式**：`#EXTM3U` 列表、JSON 顶层数组、任意层级嵌套数组（`data.list` / `rows` / `result`）、JSON 字典 `{"频道名":"url"}`、纯文本多地址、单个流地址。相对地址按接口地址补全，GBK 编码自动兜底，gzip 响应自动解压。

### adb 一键导入

```bash
adb install -r FlysnowTV-Mobile.apk

# 标准 .m3u / .txt 聚合订阅地址（占位示例，不可实际使用）
adb shell am start -a android.intent.action.VIEW -d "https://www.example.com/iptv6.m3u"

# 没有扩展名的任意接口地址（url 参数需 URL 编码）
adb shell am start -a android.intent.action.VIEW -d "flysnow://import?url=https%3A%2F%2Fx.com%2Fapi%2Flist"

# 需要请求头的第三方接口
adb shell am start -a android.intent.action.VIEW -d "https://x.com/api/list" \
  --es headers "Referer: https://x.com/"
```

## 设置中心

右上角「设置」进入，含返回按钮，触屏点选 / 切换取值即可，返回后设置立即生效。

| 分组 | 作用 |
| --- | --- |
| 一键导入订阅链接 | 粘贴链接或剪贴板一键导入，逐条校验频道数 |
| 扫码 / 局域网配置 | 启动局域网配置页（手机同 WiFi 打开网页配置订阅、接口、Token、画质等） |
| 第三方接口 / 订阅备份 | 多源管理、编辑 / 停用 / 排序 / 校验 / 删除、导出 / 导入备份 |
| Token 定时刷新 | 全局刷新周期、立即刷新、状态 |
| XMLTV 节目单 | 地址、刷新周期、立即刷新、状态 |
| 性能 | 卡片预览开关、返回频道墙后台续播（手机省电建议保持关闭） |
| 多线路 / 自动切换 | 线路失败自动切换默认开、多线路状态 |
| EPG 时间轴 / 节目预约 | 网格视图、预约提醒、查看 / 清空预约 |
| 收藏 / 最近观看 | 收藏夹、最近观看记录管理 |
| 画质分辨率 | 自动（按实测网速优选）/ 最高 / 1080p / 720p / 480p / 省流 |
| 播放引擎 | 内置标准 / 内置加速（默认）/ VLC / MX / Kodi / 系统播放器 |
| 台标来源 | 自动 / 只用内置 / 自定义 / 关闭 + 地址模板 |
| 播放诊断 | 设备分级、查看诊断日志、生成日志文件、解码失败自动换播放器 |
| 关于 | 版本号、版权、项目主页、版本发布页、日志反馈（扫码 / 微信 / QQ 分享） |

> 画质设置只对**多码率 HLS** 有意义；外部播放器无法携带自定义请求头，也不受 App 内画质设置约束。

## XMLTV 节目单

在「设置 → XMLTV 节目单」填地址（也支持扫码页填写），支持 `.xml` 与 `.xml.gz`。解析标准 `<channel><display-name>` 与 `<programme start/stop/channel>`，含时区偏移。

- 频道匹配顺序：`tvg-id` 精确 → `tvg-id` 前缀 → `display-name` 精确 → CCTV 频道号宽松匹配 → 中文台名包含。
- 播放点 `⚙` →「节目单」呼出当前 / 下一节目列表；「时间轴」打开网格视图，一次看多个频道 × 6 小时，当前时间竖线标示。
- **预约提醒**：应用在前台时定期检查，开播前 1 分钟弹出提醒，点击直接跳台；已触发的预约自动移除。
- 换台后控制条显示当前节目；节目缓存写 App 内部，断网仍可看当天节目。

## 已知限制

- 只支持 HTTP(S) 的 HLS（m3u8）/ 渐进式视频；**不支持** udp/rtmp 组播、DRM 加密流。
- 手机端为移动网络 / WiFi 场景设计，硬解能力因机型而异；4K / 高码率源在部分机型可能无法硬解，可在设置里限制画质或改用加速引擎。
- 应用不内置任何频道，全部频道来自你添加的订阅源；订阅地址失效时频道会播放失败，请更换或更新订阅。
- 回看 / 时移完全取决于源：源不支持时多数会直接报错，请选「回到直播」；App 不做本地录制。
- 预约提醒是**应用前台提醒**，退出 App 后不会响。
- XMLTV 频道匹配依赖 `tvg-id` / `display-name` 命名规范；命名完全不同的源可在 m3u 里补 `tvg-id`。
- 调用外部播放器时无法传递自定义请求头，且不受 App 内画质设置约束。
- 局域网配置服务只在二维码页打开期间运行，且需手机与设备在同一局域网。

## 重新打包

**本工程可用命令行独立编译**（Gradle 8.7 / AGP 8.5.2 / Kotlin 1.9.24 / JDK 17 / SDK 34），不依赖 Android Studio。需自备 JDK 17 与 Android SDK 34（platforms/android-34、build-tools/34.0.0）。在项目根目录执行：

```bash
export JAVA_HOME=/path/to/jdk17
export ANDROID_HOME=/path/to/android-sdk
./gradlew --no-daemon assembleRelease
```

Windows PowerShell：把前两行换成 `$env:JAVA_HOME = "..."` / `$env:ANDROID_HOME = "..."`，再执行 `.\gradlew.bat --no-daemon assembleRelease`。

产物为 `app/build/outputs/apk/release/FlysnowTV-Mobile.apk`。工程已附带 `gradlew` / `gradlew.bat` / `gradle-wrapper.jar`，Android Studio 打开也能直接 Sync；`settings.gradle` 配了阿里云 Maven 镜像优先 + 官方源兜底，`gradle.properties` 里禁止 AGP 联网补 SDK（国内直连 Google 源会卡住）。

> release 签名需在 `keystore/` 下自备 `release.jks` 与 `keystore.properties`（字段名见 `app/build.gradle`），两者均不入库。自用侧载也可直接执行 `./gradlew assembleDebug`。

## 代码结构

```
app/src/main/java/com/iptv/tvplayer/
├─ MainActivity.kt          频道墙 / 播放双模式、播放器控制条、浮层显隐、线路切换、时移、EPG、Token 定时、冷启动续播
├─ TouchGestures.kt         手机版专用触摸手势：单击 / 双击 / 左右滑换台 / 上下滑音量亮度
├─ TextLogoDrawable.kt      无 tvg-logo 时的文字台标兜底（频道名生成圆角深蓝底图）
├─ LogoFitView.kt           台标等比缩放完整显示
├─ EpgGridActivity.kt       EPG 时间轴网格视图、节目预约与跳台
├─ App.kt                   启动初始化：TLS1.2 兼容、设备分级、崩溃日志
├─ TlsCompat.kt             老设备强制启用 TLSv1.2（全局 SSLSocketFactory）
├─ DeviceProfile.kt         自动识别设备性能，决定 HLS 与解码策略
├─ SubscriptionImporter.kt  订阅链接一键导入：URL 提取、批量、真实校验
├─ Diagnostics.kt           运行日志、播放错误记录、日志文件导出
├─ FeedbackReporter.kt      日志反馈分享（扫码 / 微信 / QQ）
├─ SettingsActivity.kt      设置中心：扫码、接口、Token、XMLTV、性能、多线路、时间轴、画质、关于
├─ QrConfigActivity.kt      二维码页（ZXing 编码 + 启动/停止配置服务）
├─ WebConfigServer.kt       局域网 HTTP 配置服务 + 内嵌网页
├─ TokenManager.kt          {token} 替换、Token 获取与定时刷新
├─ EpgRepository.kt         XMLTV 拉取 / gzip / 解析 / 缓存 / 频道匹配 / 节目时间轴
├─ Prefs.kt                 设置、收藏 / 最近 / 预约缓存、线路记忆、变更版本戳
├─ ChannelExtras.kt         收藏 / 最近观看稳定标识、虚拟分组常量
├─ LogoResolver.kt          台标来源解析
├─ EpgAdapter.kt            节目单列表（含当前节目）
├─ ApiPlaylistParser.kt     通用第三方 JSON 接口解析
├─ SourceConfig.kt          接口配置：GET/POST、请求头、Token、字段映射
├─ PlaylistRepository.kt    多源拉取、POST、Token 注入、缓存（含备用线路）、GBK 兜底
├─ PlaylistParser.kt        m3u 解析（tvg-id / tvg-logo / catchup / #FX-ALT 备用线路）
├─ Ipv6PreferredDns.kt      IPv6 优先 DNS：AAAA 记录排前、失败回退 IPv4
├─ ChannelAdapter.kt        海报卡片：台标、收藏角标、长按收藏
├─ GroupAdapter.kt          顶部横滑分类标签条
├─ DialogFitter.kt          弹窗按内容自适应宽高
└─ Channel.kt               频道数据模型（备用线路、回看模板、tvgId）
scripts/gen_assets.py       生成 launcher 图标 / 横幅 / 界面标记（零依赖）
scripts/round_launcher_corners.py  图标圆角烘焙（幂等）
```

## 免责声明

本项目仅提供直播流的**解析与播放能力**，不内置、不分发、不提供任何频道内容或订阅地址。使用者应确保所导入订阅的合法性与授权，自用测试请勿分发。因使用第三方订阅产生的任何后果由使用者自行承担。
