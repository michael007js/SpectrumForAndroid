**SpectrumForAndroid**
 

[一个简单且强大的exo3+播放器库](https://github.com/michael007js/SimpleSuperExoPlayer/tree/master)

[一个android平台下的稀有自绘控件集合](https://github.com/michael007js/SimpleViewSet)


 **项目介绍**

 这是一个Android平台的音乐频谱合集,把音频能量用canvas画出来，至于频谱数据的获取则[用java原生实现了快速傅里叶变换](https://github.com/michael007js/SpectrumForAndroid/tree/master/app/src/main/java/com/sss/fourier)，同时也包含了第三方jar包[数字信号处理包（TarsosDSP-Android）](https://github.com/michael007js/SpectrumForAndroid/tree/master/app/libs)

 本项目与市面上大多数同类项目的区别是实现方式更优雅，不需要通过申请录音权限进而使用Visualizer来实现（大清亡了啊喂！还在使用Visualizer），而是通过实现AudioProcessor接口直接对原始音频数据作运算处理，
 
 这也是本项目的亮点，特此开源，有需要的童鞋可以拿来一看

No picture u say a j8!

少啰嗦，先看效果

![上帝之环](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_god_ring.gif "上帝之环")
![扩散环](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_diffusion_ring.gif "扩散环")
![仿网易云音乐](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_attachment_ring.gif "仿网易云音乐")
![多彩泡泡](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_color_bubble.gif "多彩泡泡")
![仿AI语音](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_ai_voice.gif "仿AI语音")
![贝塞尔](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_bessel.gif "贝塞尔")
![水波扩散](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_circle_round.gif "水波扩散")
![柱状](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_columnar.gif "柱状")
![随机棋盘](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_grid.gif "随机棋盘")
![六芒星](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_hexagram.gif "六芒星")
![横向能量条](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_horizontal_energy.gif "横向能量条")
![旋转头像](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_rotating_circle.gif "旋转头像")
![人工智障Siri](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_siri.gif "人工智障Siri")
![伸缩球](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_slip_ball.gif "伸缩球")
![里程表](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_speedometer.gif "里程表")
![声波](https://github.com/michael007js/SpectrumForAndroid/blob/master/gif/demo_wave.gif "声波")

 个人兴趣，本项目会持续更新
 
 over
 
 By SSS





