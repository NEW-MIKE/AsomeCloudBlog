# 211018_函数
#函数是一段封装的代码,在用到它时进行调用
# def fun1():
#     print('Hello')
# fun1()  #调用函数
#
# def fun2():
#     return 'Hello'
# print(fun2())

# sin(30°)=0.5
# sin(45°)=2**0.5/2
# sin(90°)=1

#写一个函数,可以计算两数之和
# def sumdata(a,b):  #a,b形式参数,简称形参
#     return a+b
# print(sumdata(20,72))  #实际参数,简称实参
# print(sumdata(20))  #报错,实参数量小于形参数量
# print(sumdata(20,30,50))  #报错,实参数量大于形参数量

#函数的缺省值
# def sumdata2(a=100,b=180):  #a,b形式参数,简称形参
#     return a+b
# print(sumdata2())  #没有传参数,使用函数的缺省值
# print(sumdata2(9))  #传一个参数,替换a的值
# print(sumdata2(b=9))  #传一个参数,替换b的值
# print(sumdata2(3,6))  #用户输入值时,使用用户传的值
# print(sumdata2(3,b=9))  #先简略写法,后完整写法
# print(sumdata2(a=3,9))  #先完整写法,后简略写法,这种写法会报错

#函数可以出现多个return
#写一个函数,返回某数的绝对值
# def jueduizhi(n):
#     if n>=0:
#         return n
#         return '2021年10月18日'  #在第一个return之后,程序退出了函数,之后的语句称之为不可达语句
#     else:
#         return -n
# print(jueduizhi(6))

#函数能否return多个值,有多个值时,以元组形式返回
# def sumdata3(a,b):
#     return a+b,a-b,a*b,a**b
# print(sumdata3(3,6))

# def sumdata3(a,b):
#     return [a+b,a-b,a*b,a**b]  #列表是一个对象,不需要以元组形式返回
# print(sumdata3(3,6))

#可变长度参数
# *args,允许用户输入任意个参数
# def fun6(a,*args):
#     return a,*args  #这种写法,可以少一层元组,在低版本的python中可能会报错
# print(fun6(1,2,3,4,5))

# def fun6(a,*args):
#     return (a,*args)  #低版本的python,这么写
# print(fun6(1,2,3,4,5))

# def fun6(a,*args):
#     return a,args  #这种写法,会多一层元组
# print(fun6(1,2,3,4,5))

#关键字参数
#**kwargs  允许用户输入任意个参数,用户传的参数必须是a=b的格式,返回值是字典类型
# def fun9(**kwargs):
#     return kwargs
# print(fun9(name='德华',age=18))

#print()函数的sep=' ',每个参数之间的间隔符
# print(1,2,3,4,5,sep='//')

#end='\n',每次执行完毕之后补充一个换行符
# print(1,end='')
# print(2,end='')

#课堂小结
#函数的特性
#函数的返回值
#形参与实参
#*args,**kwargs
#print()函数的sep=' ',end='\n'