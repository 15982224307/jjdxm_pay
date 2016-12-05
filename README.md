# [jjdxm_pay][project] #
### Copyright notice ###

我在网上写的文章、项目都可以转载，但请注明出处，这是我唯一的要求。当然纯我个人原创的成果被转载了，不注明出处也是没有关系的，但是由我转载或者借鉴了别人的成果的请注明他人的出处，算是对前辈们的一种尊重吧！

虽然我支持写禁止转载的作者，这是他们的成果，他们有这个权利，但我不觉得强行扭转用户习惯会有一个很好的结果。纯属个人的观点，没有特别的意思。可能我是一个版权意识很差的人吧，所以以前用了前辈们的文章、项目有很多都没有注明出处，实在是抱歉！有想起或看到的我都会逐一补回去。

从一开始，就没指望从我写的文章、项目上获得什么回报，一方面是为了自己以后能够快速的回忆起曾经做过的事情，避免重复造轮子做无意义的事，另一方面是为了锻炼下写文档、文字组织的能力和经验。如果在方便自己的同时，对你们也有很大帮助，自然是求之不得的事了。要是有人转载或使用了我的东西觉得有帮助想要打赏给我，多少都行哈，心里却很开心，被人认可总归是件令人愉悦的事情。

站在了前辈们的肩膀上，才能走得更远视野更广。前辈们写的文章、项目给我带来了很多知识和帮助，我没有理由不去努力，没有理由不让自己成长的更好。写出好的东西于人于己都是好的，但是由于本人自身视野和能力水平有限，错误或者不好的望多多指点交流。

项目中如有不同程度的参考借鉴前辈们的文章、项目会在下面注明出处的，纯属为了个人以后开发工作或者文档能力的方便。如有侵犯到您的合法权益，对您造成了困惑，请联系协商解决，望多多谅解哈！若您也有共同的兴趣交流技术上的问题加入交流群QQ： 548545202

感谢作者[lbh][author]，本项目基于[libPaySdk][url]项目进行支付宝的升级和删减了银联的开发完成的，调整了大部分的调用方式和配置习惯。

## Introduction ##
该组件为封装了 微信，支付宝（2.0版本的）， 一键快速集成。


## Features ##

## Screenshots ##

<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_pay/master/screenshots/icon01.png" width="300"> 
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_pay/master/screenshots/icon02.png" width="300"> 
 
## Download ##

[demo apk下载][downapk]

Download or grab via Maven:

	<dependency>
	  <groupId>com.dou361.pay</groupId>
	  <artifactId>jjdxm-pay</artifactId>
	  <version>x.x.x</version>
	</dependency>

or Gradle:

	compile 'com.dou361.pay:jjdxm-pay:x.x.x'


历史版本：
	compile 'com.dou361.pay:jjdxm-pay:1.0.2'
	compile 'com.dou361.pay:jjdxm-pay:1.0.1'  支付宝2.0
	compile 'com.dou361.pay:jjdxm-pay:1.0.0'

jjdxm-pay requires at minimum Java 9 or Android 2.3.


[架包的打包引用以及冲突解决][jaraar]

## Proguard ##

类库中使用consumerProguardFiles属性，它指定了编译时，库自动引入的混淆规则。也就是说应用打包时候会自动的寻找库里的混淆文件，不需要手工配置了。


[AndroidStudio代码混淆注意的问题][minify]

## Get Started ##

### step1 ###
需要申请的一些权限已经集成到类库中了,引入依赖，如果主程序项目中有重复的类库，可以用打开注释来移除重复依赖。

	    compile ('com.dou361.pay:jjdxm-pay:1.0.2'){
	//        exclude group: 'com.dou361.alipay', module: 'jjdxm-alipay'
	//        exclude group: 'com.dou361.wechat', module: 'jjdxm-wechat'
	    }

### 支付参数说明 ###
PayType:
支付的支付方式，目前支持：

	PayAgent.PayType.WECHATPAY（微信支付）；
	PayAgent.PayType.ALIPAY（支付宝）；

支付使用的对象，支付宝的以下参数都是需要传的，微信只有price和orderNo是必须传的。支付宝中价格price可以允许传小数点例如：0.01单位为元；微信中价格price不能有小数点，例如1单位为分。

PayInfo：

	/** 商品名称*/
	private String subject;
	
	/** 商品详细信息  商品的标题/交易标题/订单标题/订单关键字等。该参数最长为128个汉字*/
	private String body;
	
	/** 商品价格*/
	private String price;
	
	/** 商品订单号*/
	private String orderNo;
	
	/** 支付通知地址*/
	private String notifyUrl;

OnPayListener：
支付监听器：

	onStartPay() 开始支付，可以在此做 支付前准备提示
	onPaySuccess(); 支付成功
	onPayFail(String code, String msg); 支付失败，code和msg 均为第三方原样返回

### step2 ###
#### 配置 AndroidManifest ####

 	<!-- 微信支付 start -->
    <activity
        android:name=".wxapi.WXPayEntryActivity"
        android:launchMode="singleTop"
        android:exported="true"
        android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
    <!-- 微信支付 end -->

### step3 ###
#### 初始化支付组件 ####

		PayAgent.getInstance()
                .init(this)
                .setDebug(true)
                .initPay(appId,partnerId,privateKey,appwxId,mchId,appKey);
        
其中initPay方法的参数说明如下：

	/***
     * 初始化所有
     * @param appId 必填：支付宝app_id
     * @param partnerId 必填：支付宝商户id
     * @param privateKey 可填：支付宝私有key，订单加签使用，如果使用服务器签好名的字符串可不传该参数
     * @param appwxId 必填：微信app_id
     * @param mchId 必填：微信商户id
     * @param appKey 可填：微信key，订单加签使用，如果使用服务器签好名的字符串可不传该参数
     * @return
     */
	PayAgent initPay(String appId, String partnerId, String privateKey, String appwxId, String mchId,
                            String appKey)
### step4 ###
#### 两种支付方式 ####

1.app内对订单信息加签，然后调起支付，这种方式需要传递上面六个参数appId，partnerId，privateKey，appwxId，mchId，appKey，可以用作demo调试使用

	/***
     * 调起 支付 app内处理加签操作，出于安全不建议使用，可以用作demo调试
     * @param payType  支付类型
     * @param activity 所在的activity
     * @param payInfo 支付对象信息[订单号，支付金额，商品名称，支付服务器回调地址..]微信:交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。
     * @param listener 支付回调
     * @return
     */
    public PayAgent onPay(PayType payType, Activity activity, PayInfo payInfo, OnPayListener listener)

2.服务器中对订单信息加签，然后作为参数payInfo回传，然后调起支付，这种方式上面的支付宝的私有key privateKey和微信的appKey可以不传，传入null值或其他默认值即可代替即可。

	/***
     * 调起 支付 服务器加载后，回传加签数据
     * @param payType  支付类型
     * @param activity 所在的activity
     * @param payInfo 服务器加签后的数据 例如：{"appid":"wxb4ba3c02aa476ea1","partnerid":"1305176001","package":"Sign=WXPay","noncestr":"6e778396de67b4a57310651d3302067c","timestamp":1473258151,"prepayid":"wx20160907222231ce5961f6a10963609051","sign":"E0C61AD6F45E26D20B8B4F2B69B4170B"}
     * @param listener 支付回调
     * @return
     */
    public PayAgent onPay(PayType payType, Activity activity, String payInfo, OnPayListener listener)


#### 调起支付 ####


        PayAgent.getInstance().onPay(payType, this, payInfo,
				new OnPayListener() {

					@Override
					public void onStartPay() {
					}

					@Override
					public void onPaySuccess() {
						Toast.makeText(MainActivity.this,"支付成功！", 1).show();
					}

					@Override
					public void onPayFail(String code, String msg) {
						Toast.makeText(MainActivity.this,
								"code:" + code + "msg:" + msg, 1).show();
						Log.e(getClass().getName(), "code:" + code + "msg:" + msg);
					}
				});


## More Actions ##

### 微信支付接入准备和注意事项 ###

1.注册微信开放平台：https://open.weixin.qq.com
开发者资质认证300元一年
【新建应用】【审核】【通过】【申请开通支付能力】
2.注册微信商户平台：https://pay.weixin.qq.com
【商户认证】【认证通过】【绑定应用支付能力】
注：一个商户只能绑定一个应用

3.后台设置
商户在微信开放平台申请开发应用后，微信开放平台会生成APP的唯一标识APPID。由于需要保证支付安全，需要在开放平台绑定商户应用包名和应用签名，设置好后才能正常发起支付。设置界面在【开放平台】中的栏目【管理中心 / 修改应用 / 修改开发信息】里面，如下图：

<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_pay/master/screenshots/chapter1.png" width="300"> 

应用包名：是在APP项目配置文件AndroidManifest.xml中声明的package值，例如DEMO中的package="net.sourceforge.simcpux"。
应用签名：根据项目的应用包名和编译使用的keystore，可由签名工具生成一个32位的md5串，在调试的手机上安装签名工具后，运行可生成应用签名串，绿色串即应用签名。签名工具下载，如下图：

<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_pay/master/screenshots/chapter2.png" width="300"> 

4.回调中errCode值列表：

<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_pay/master/screenshots/chapter3.png" width="500">

### 支付宝支付接入准备和注意事项 ###

第一步：创建应用并获取APPID

要在您的应用中使用支付宝开放产品的接口能力，您需要先去蚂蚁金服开放平台（open.alipay.com），在管理中心中创建登记您的应用，并提交审核，审核通过后会为您生成应用唯一标识（APPID），并且可以申请开通开放产品使用权限，通过APPID您的应用才能调用开放产品的接口能力。需要详细了解开放平台创建应用步骤请参考《开放平台应用创建指南》。

第二步：配置密钥

开发者调用接口前需要先生成RSA密钥，RSA密钥包含应用私钥(APP_PRIVATE_KEY)、应用公钥(APP_PUBLIC_KEY）。生成密钥后在开放平台管理中心进行密钥配置，配置完成后可以获取支付宝公钥(ALIPAY_PUBLIC_KEY)。详细步骤请参考《配置应用环境》。

## ChangeLog ##

## About Author ##

#### 个人网站:[http://www.dou361.com][web] ####
#### GitHub:[jjdxmashl][github] ####
#### QQ:316988670 ####
#### 交流QQ群:548545202 ####


## License ##

    Copyright (C) dou361, The Framework Open Source Project
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
     	http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

## (Frequently Asked Questions)FAQ ##
## Bugs Report and Help ##

If you find any bug when using project, please report [here][issues]. Thanks for helping us building a better one.




[web]:http://www.dou361.com
[github]:https://github.com/jjdxmashl/
[project]:https://github.com/jjdxmashl/jjdxm_pay/
[issues]:https://github.com/jjdxmashl/jjdxm_pay/issues/new
[downapk]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_pay/master/apk/app-debug.apk
[lastaar]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_pay/master/release/jjdxm-pay-1.0.0.aar
[lastjar]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_pay/master/release/jjdxm-pay-1.0.0.jar
[icon01]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_pay/master/screenshots/icon01.png
[icon02]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_pay/master/screenshots/icon02.png
[jaraar]:https://github.com/jjdxmashl/jjdxm_ecodingprocess/blob/master/架包的打包引用以及冲突解决.md
[minify]:https://github.com/jjdxmashl/jjdxm_ecodingprocess/blob/master/AndroidStudio代码混淆注意的问题.md
[author]:https://git.oschina.net/lbh
[url]:https://git.oschina.net/lbh/libPaySdk