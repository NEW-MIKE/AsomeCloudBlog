# 211101_异常与日志
#异常就是程序运行时出现了错误
#try except语句,至少有一个except,也可以有多个,也可以有一个else语句,一个finally语句
# try:
#     input1=int(input('请输入一个数字:  '))
#     print(1/input1)
# except ZeroDivisionError:  #0作为分母的异常
#     print('0不能作为分母')
# except ValueError:  #非数字异常
#     print('您输入的不是数字')
# except:  #不指定异常类型,则捕获任何异常
#     print('程序出现错误')
# else:  #当程序没有出现任何异常,则执行else中的语句
#     print('没有出现错误')
# finally:  #无论程序是否出现异常,都会执行
#     print('程序运行完毕')

#常见的异常
# NameError  未定义的变量
# print(str1)
# IndexError  下标越界
# list1=[100]
# print(list1[1])
# FileNotFoundError  找不到文件异常
# with open('D:/cef.txt') as file1:
#     file1.read()

#所有的异常,都是Exception的子类,或者子类的子类
# print(NameError.__base__)
# print(IndexError.__base__)
# print(LookupError.__base__)
# print(FileNotFoundError.__base__)
# print(OSError.__base__)

# try:
#     raise IOError  #假装这里有异常
# except IOError:
#     print('程序出现了IO异常')

#日志
#logging模块,可以配合try except记录日志
# import time
# import logging
# import traceback
# logging.basicConfig(level='DEBUG',filename='D:/211101_log1.log',filemode='a')
# logging.debug('松勤')
# logging.info('松勤')
# logging.warning('松勤')
# logging.error('松勤')
# logging.critical('松勤')
# try:
#     input1=int(input('请输入一个数字:  '))
#     print(1/input1)
# except:  #0作为分母的异常
#     logging.error(time.strftime('%y-%m-%d %H:%M:%S')+traceback.format_exc())

import os
from loguru import logger
# logger.debug('松勤')
# logger.info('松勤')
# logger.warning('松勤')
# logger.error('松勤')
# logger.critical('松勤')

if not os.path.exists('./log'):
    os.mkdir('./log')
logfile='./log/log1.log'
logger.add(logfile,rotation='200KB',compression='zip')  #rotation拆分日志文件,每个日志200KB,compression压缩日志
# logger.remove(handler_id=None)
# for i in range(10000):
#     logger.info('松勤')
def fun1(a,b):
    return a/b
try:
    fun1(2,0)
except:
    logger.exception('报错')

#课堂小结
#try,except,else,finally
#常见的异常,所有的异常都是Exception的子类,或者子类的子类
#手动抛出异常  raise
#logging模块  日志级别 debug<info<warning<error<critical
#traceback.format_exc()
#loguru模块