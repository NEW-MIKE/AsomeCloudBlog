# 211017_布尔表达式
#布尔值True,False  真,假  1,0
#python中,=表示赋值,==表示判断恒等,!=表示判断不相等
# print(100==200)
# print(100!=200)
# print('a'=='A')  #字符串之间的比较,根据ASCII码进行判断,a=97,A=65
# print('aA'>'Aa')  #字符串的比较,只比较第一位,当第一位相同时,比较第二位






#in,not in
# list1=[100,200,[300,400,500]]
# print(100 in list1)
# print(500 in list1)  #500属于子列表,不属于外层的列表
# print(500 not in list1)  #not True为False,not False为True

#and,or
# print(3>2 and 2>1 and 1>2)  #一假为假,全真为真
# print(3>2 or 1>2 or 2>3)  #一真为真,全假为假

# print(1>2 and ....)  #程序执行时,只要它能判断式子为假,那么后面的式子就可以不用执行,直接返回False
# print(2>1 or ...)  #程序执行时,只要它能判断式子为真,那么后面的式子就可以不用执行,直接返回True

#not,and,or组合条件语句  not>and>or
# print(2>1 and 1>2 and not True or 3>2)
# print(2>1 and 1>2 and (not True or 3>2))

#浅拷贝,深拷贝
#赋值
list2=[100,200,300,[400,500,600]]
# list2_new=list2  #对列表赋值时,相当于起了一个别名,两个变量名指向的是同一个对象
# list2[0]=999
# print(list2,id(list2))
# print(list2_new,id(list2_new))

#浅拷贝
# import copy
# list2_new=copy.copy(list2)  #浅拷贝,生成了新的对象,子列表仍然是同一个对象
# list2[0]=999
# list2[-1][0]=680
# print(list2,id(list2),id(list2[-1]))
# print(list2_new,id(list2_new),id(list2_new[-1]))
#切片,等价于浅拷贝
# list2_new=list2[:]
# list2[0]=999
# list2[-1][0]=680
# print(list2,id(list2),id(list2[-1]))
# print(list2_new,id(list2_new),id(list2_new[-1]))

#深拷贝
# list2_new=copy.deepcopy(list2)  #深拷贝,生成了新的对象,子列表也是新的对象
# list2[0]=999
# list2[-1][0]=680
# print(list2,id(list2),id(list2[-1]))
# print(list2_new,id(list2_new),id(list2_new[-1]))

#课堂小结
#True,False
#in,not in
#and,or
#条件组合与优先级
#浅拷贝,深拷贝