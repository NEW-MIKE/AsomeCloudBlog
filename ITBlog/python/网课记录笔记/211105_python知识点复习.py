# 211105_python知识点复习
#字符串,可以用单引号表示,也可以用双引号表示,也可以用三引号表示
#三引号除了可以表示字符串之外,也可以在类或方法或函数中作为注释
# def fun1():
#     '''
#     :return:
#     '''
# print(fun1.__doc__)

#字符串的切片  [起始值,终止值,步长]
#包含起始值,不包含终止值,步长缺省值为1
str1='ynracev'
# print(str1[3:5])  #ac
# print(str1[-4:-2])  #ac
# print(str1[-3:-5:-1])  #ca

#切片生成的是一个新的对象,不影响原对象
# str1_new=str1[-3:-5:-1]
# print(str1_new)
# print(str1)

#[::-1]翻转
# list1=[100,8,92,3,6]
# list1_new=list1[::-1]
# print(list1_new)
#反向排序
# list1_new=sorted(list1,reverse=True)
# print(list1_new)
#判断某个字符串是否是回文
# str2='上海自来水来自海上'
# str3='山西悬空寺空悬西山'
# def fun1(str):
#     if str[::-1]==str:
#         return True
#     else:
#         return False
# print(fun1(str3))

#列表
#新增元素  append,insert,extend
#删除元素  pop,remove,del
#打印斐波那契数列的前20位
# 1,1,2,3,5,8,13,21,34,55,89......
# list2=[]
# for i in range(20):
#     if i<2:
#         list2.append(1)
#     else:
#         list2.append(list2[-2]+list2[-1])
# print(list2)

#列表的切片,用法与字符串的切片相同,生成的也是新的对象
#元组也可以使用下标与切片,但是不能增删改,属于不可变对象

#假设你的老板让你测试一个接口,A接口数据是[100,98,62,36,[54,60]],B接口的数据与A接口相同
# 此时需要修改A接口的值,把第0位改为68,但是不可以修改B接口的值
#然后老板要求修改子列表的第0位为96
interfaceA=[100,98,62,36,[54,60]]
# interfaceB=interfaceA  #赋值,相当于起了一个别名,两个变量指向的是同一个对象
# interfaceA[0]=68
# print(interfaceA)
# print(interfaceB)

import copy
# interfaceB=copy.copy(interfaceA)  #浅拷贝,生成了新的对象,子列表仍然是同一个对象
# interfaceA[0]=68
# interfaceA[-1][0]=96
# print(interfaceA)
# print(interfaceB)

#完整的切片,等价于浅拷贝
# interfaceB=interfaceA[:]  #浅拷贝,生成了新的对象,子列表仍然是同一个对象
# interfaceA[0]=68
# interfaceA[-1][0]=96
# print(interfaceA)
# print(interfaceB)

# interfaceB=copy.deepcopy(interfaceA)  #深拷贝,生成了新的对象,子列表也是不同的对象
# interfaceA[0]=68
# interfaceA[-1][0]=96
# print(interfaceA)
# print(interfaceB)

#布尔表达式
# if 12345:  #0,'',None相当于False
#     print('表达式为真')

# print(2>1 and 1>5 or 3>4 or 6>5 and 2>1 and not 3>2)

# score=input('请输入一个数字:  ')  #input()从键盘读取用户输入的值,返回值是str型
# if not score.isdigit():  #isdigit()判断对象是否是纯数字
#     print('您输入的不是数字')
# else:  #不满足if条件,则执行else中的语句
#     score=int(score)  #将score转为int型
#     if score>=90:
#         print('优秀')
#     elif score>=80:
#         print('不错')
#     elif score>=60:
#         print('及格')
#     else:
#         print('不及格')

#函数
#写一个函数,用户输入一个参数n,可以计算从1到n的和
# def sum1(n):
#     sum=0
#     for i in range(1,n+1):
#         sum+=i
#     return sum
# print(sum1(100))

#写一个函数,可以求某数的阶乘,尽量不使用循环
# 6!=6*5*4*3*2*1=6*5!
# 5!=5*4*3*2*1=5*4!
# 4!=4*3*2*1=4*3!
# def jiecheng(n):
#     if n==1:
#         return 1
#     else:
#         return n*jiecheng(n-1)  #递归,函数自己调用自己
# print(jiecheng(6))

# import time
# for i in range(10,0,-1):
#     print(f'\r倒计时{i}秒',end='')
#     time.sleep(1)
# else:
#     print('\r倒计时结束')

#文件的读写
#read(),readline(),readlines(),read().splitlines()
#seek(0)  光标回到文件首位

#w+  可以同时读写,如果文件不存在,则新建文件,写入时是清空写入
#r+  可以同时读写,如果文件不存在,则报错,写入时是覆盖写入
#a+  可以同时读写,如果文件不存在,则新建文件,写入时是追加写入

#字典
#字典是无序的
#字典的键是唯一的
#字典的新增和修改使用相同的代码
# set1={1,2,3,4,5,5}  #集合,相当于只有键的字典
# print(set1)

#遍历字典 items()

# json.loads()  #json转字典
# json.dumps()  #字典转json

#模块与包
#查看标准路径  sys.path