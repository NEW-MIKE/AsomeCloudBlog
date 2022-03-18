# 211025_模块与包
#一个.py文件就是一个模块
#模块命名时,以英文开头,尽量不用中文作为模块名
#包是一种特殊的文件夹,如果目录里有__init__.py文件,则这个文件夹就是包
#当导入包时,__init__.py文件里的代码会立刻执行一次
# import AUTO55

#导入模块的几种形式
# import 模块名  适用于与当前模块在同一个目录内,或者位于标准库/第三方库中的文件
#使用模块中的函数时,方式是模块名.函数名()
# import sumdata1
# print(sumdata1.sumdata(3,6))

#from 包 import 模块
#调用时,执行模块.函数名()
# from AUTO54 import sumdata1
# print(sumdata1.sumdata(1,2))

#from 包.模块 import 函数
# from AUTO54.sumdata1 import sumdata
# print(sumdata(2,3))

# from 模块 import 函数
# from sumdata1 import sumdata
# print(sumdata(3,6))

# if __name__ == '__main__':  #以下代码只在本模块运行
#     pass

#安装第三方库
#python中有标准库,第三方库,其中标准库是不需要安装,直接使用的,第三方库需要安装后使用
# import copy
# import time
# import this
# from selenium import webdriver
#安装第三方库
#在cmd中,执行pip install 库名
#如果网速比较慢,可以考虑使用国内的镜像网站进行安装
#豆瓣源
#pip install xlwings -i http://pypi.douban.com/simple/ --trusted-host pypi.douban.com
#清华源
#pip install selenium -i https://pypi.tuna.tsinghua.edu.cn/simple/  --trusted-host pypi.tuna.tsinghua.edu.cn

#安装指定版本
#pip install selenium==3.141

#查看安装了哪些第三方库
#pip list

#卸载
#pip uninstall 第三方库名

#在pycharm中安装第三方库
#settings→项目名→python interpreter,点击+号进行安装
#新建项目时,选择真实环境

#标准路径
# import QQQ

import sys
# for one in sys.path:  #显示python标准路径
#     print(one)
#标准路径的第一个是当前路径,第二个是工程路径,后面都是标准路径
#第三方库位于python目录里的\lib\site-packages

sys.path.append('D:/PKG21')  #将路径添加到标准路径
import QQ123
#永久添加到标准路径,进入site-packages目录,新建一个pth文件,文件名任意,后缀名必须是.pth,将路径写入文件中