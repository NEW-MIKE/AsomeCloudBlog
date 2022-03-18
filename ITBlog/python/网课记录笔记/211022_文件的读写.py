# 211022_文件的读写
filepath='D:/note1.txt'
# file1=open(filepath)  #打开一个文件,不写模式的情况下是读取模式
# print(file1.read())  #读取文件内容
# file1.close()  #关闭文件,使用open()时,必须要有close(),否则会一直占用内存

# file1=open(filepath,'w')  #打开一个文件,选择写入模式
# file1.write('ABC')
# print(file1.read())  #w模式只可以写入,不可读取
# file1.close()  #关闭文件,使用open()时,必须要有close(),否则会一直占用内存

#w+  可以同时读写,如果文件不存在,则新建文件,写入时是清空写入
#r+  可以同时读写,如果文件不存在,则报错,写入时是覆盖写入
#a+  可以同时读写,如果文件不存在,则新建文件,写入时是追加写入

# file1=open(filepath,'a+')  #打开一个文件,选择写入模式
# file1.write('H')
# file1.close()  #关闭文件,使用open()时,必须要有close(),否则会一直占用内存

#r+  ABC  写入Q   QBC
#w+  ABC  写入Q   Q
#a+  ABC  写入Q   ABCQ

# file1=open(filepath,'w+')  #打开一个文件,选择写入模式
# file1.write('春风得意马蹄疾\n一日看尽长安花')
# #使用seek()时,要注意中文占两个字节
# # file1.seek(2)  #光标回到文件首位,里面的数字控制回到文件首位之后,偏移多少位
# #seek()一共有两个参数,第二个参数默认为0,表示回到文件首位
# # 如果第二个参数为1,表示保持光标当前位置,如果第二个参数为2,表示光标移动到文件末尾,第二个参数为1,2时,只有wb模式可以使用
# print(file1.read())
# file1.close()  #关闭文件,使用open()时,必须要有close(),否则会一直占用内存

filepath2='D:/note3.txt'
# file2=open(filepath2)
# # print(file2.read())  #读取文件所有内容,返回值是字符串
# # print(file2.readline())  #每次读取一行的内容
# # print(file2.readlines())  #读取文件所有内容,返回值是列表
# print(file2.read().splitlines())  #读取文件所有内容,返回值是列表,不出现\n
# file2.close()

#with open(),用法与open()一样,优点:它可以处理多个文件,不需要写close()方法
# filepath3='D:/211022_1.txt'
# with open(filepath2) as file2,open(filepath3,'w+') as file3:
#     print(file2.read().splitlines())
#     file3.write('君问归期未有期,巴山夜雨涨秋池.何当共剪西窗烛,却话巴山夜雨时')

#性能测试,要求快速生成1000个用户的账号,账号格式sq001,123456
with open('D:/zhanghao_211022.txt','w+') as file6:
    for i in range(1,1001):
        file6.write(f'sq{i:03},123456\n')



#课堂小结
#open(),read(),write(),close()
#w+,r+,a+
#seek()
#with open()
#read(),readline(),readlines(),read().splitlines()