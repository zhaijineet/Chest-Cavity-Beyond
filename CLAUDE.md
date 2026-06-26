不允许使用变量名简称：例如如context写成ctx，不允许
但多个单词使用其中一个单词命名是允许的：例如ItemStackData的变量名写成data。
不过如果有两种，例如ItemStackData和TooltipData，就需要把所有的单词写上了：例如ItemStackData的变量名应该是itemStackData

不允许有致死量的注释，当信息足够自解释时，不需要添加注释
若要加javadoc需要一句话写出核心内容，不能有javadoc的注释头尾和注释内容加起来只占一行的情况，必须展开
若是为Minecraft或NeoForge的方法添加javadoc时，除非是渲染相关，否则不允许添加@param

NeoForge源码： E:Mod Project\neoforge-21.1.219-merged
JEI源码：E:\Mod Project\JustEnoughItems
苹果皮源码：E:\Mod Project\AppleSkin