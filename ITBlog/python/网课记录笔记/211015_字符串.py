# 211015_字符串
#用引号引起来的,就是字符串
#python中,字符串既可以用单引号表示,也可以用双引号表示
# url='https://wwwbaidu.com'
# url2="https://www.baidu.com"
# print(url2)

# str1='It's OK'  #invalid syntax  语法错误
# str1="It's OK"  #当字符串中有单引号时,外面用双引号
# print(str1)
# str2='He said:"Are you OK?"'  #当字符串中有双引号时,外面用单引号
# print(str2)

#字符串也可以用三引号表示''' '''   """ """
# str3='飞流直下三千尺\n疑是银河落九天'
# str3_new='''飞流直下三千尺
# 疑是银河落九天'''
# print(str3_new)

#转义符  \n换行符,\t制表符
# filepath='D:\note1.txt'
#方案一,\n前面再加一个\
# filepath='D:\\note1.txt'
#方案二,字符串的前面加r,表示后面的字符串中,所有的转义符均不生效
# filepath=r'D:\note1.txt'
#方案三,表示路径时,可以用/代替\
# filepath='D:/note1.txt'
# print(filepath)

#字符串的拼接
#python中常见的数据类型,有str型,int型,float型等等
# print(1+'1')  #int型与str型进行相加,报错
# print('1'+'1')  #str型可以和str型进行拼接
# print('1'*6)  #将字符串打印6次

# a=9
# b='6'
# print(a+int(b))  #int()函数,将对象转为int型
# print(str(a)+b)  #str()函数,将对象转为str型
# print(type(a))  #type()函数,返回对象的类型

#字符串的下标
# str6='hytnbge'
# print(str6[4])  #下标从0开始
# print(str6[-3])  #负下标从-1开始
# str6[0]='c'  #字符串是不可变对象,不可以修改某一位的值
# print(str6)


#字符串的切片  [起始值:终止值:步长]  包含起始值,不包含终止值,步长的缺省值为1
id_card='320104199809090879'
# birthday=id_card[6:14]
# # birthday=id_card[-12:-4]
# birthday=id_card[-12::-1]  #步长的数字控制跨几位取值,步长为正数时,从左向右取值,步长为负数时从右向左取值
# print(birthday)

#切片是一个新的对象,不影响原对象
# birthday=id_card[6:14]
# print(id_card)

#课堂小结
#字符串中的单引号,双引号,三引号
#字符串的拼接
#转义符
#字符串的下标
#字符串的切片