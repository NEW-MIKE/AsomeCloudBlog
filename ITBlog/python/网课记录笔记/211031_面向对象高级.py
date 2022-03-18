# 211031_面向对象高级
#私有属性与私有方法
#私有属性与私有方法不能从外部被调用,也不能被子类继承
#在属性或方法的前面加上__,就变成了私有属性或私有方法,如果前后都有__,不是私有属性或私有方法
# class Class_test1:
#     __str1='ABC'  #私有属性
#     str2='DEF'
#     def __init__(self):
#         pass
#     def __method1(self):  #私有方法
#         print('这是一个私有方法')
#     def method2(self):
#         print(self.__str1)  #调用私有属性
#         self.__method1()  #调用私有方法
# cls1=Class_test1()
# print(cls1.__str1)  #报错,私有属性不能被直接调用
# cls1.__method1()  #报错,私有方法不能被直接调用
# cls1.method2()

#所有的类都是object的子类,一个类无论是否声明继承object,实际都继承
class Class2:
    '''
    爆竹声中一岁除
    春风送暖入屠苏
    '''
# print(Class2.__dict__)  #显示类的属性
# print(Class2.__doc__)  #显示类的注释
# print(Class2.__name__)  #显示类的名称
# print(Class2.__base__)  #显示父类的名称
# print(Class2.__bases__)  #显示所有父类的名称

#多继承，一个类可以有多个父类
# class Money1:
#     def money(self):
#         print('一个亿')
#
# class Money2:
#     def money(self):
#         print('两个亿')
# class Human(Money1,Money2):  #继承多个父类时，用逗号隔开，多个父类中有同名方法时，按照继承顺序进行调用
#     pass
# man=Human()
# man.money()

#多态
# class Dog:
#     def say(self):
#         print('汪汪汪')
#
# class Cat:
#     def say(self):
#         print('喵喵喵')
# dog=Dog()
# cat=Cat()
# def animal_say(obj):
#     obj.say()
# animal_say(cat)

# class Fanguan:
#     pass
# class Yuxiangrousi(Fanguan):
#     def caidan(self):
#         print('鱼香肉丝')
# class Gongbaojiding(Fanguan):
#     def caidan(self):
#         print('宫爆鸡丁')
# class Qingjiaotudousi(Fanguan):
#     def caidan(self):
#         print('青椒土豆丝')
# guke1=Yuxiangrousi()
# guke2=Gongbaojiding()
# guke3=Qingjiaotudousi()
# def fuwuyuan(obj):
#     obj.caidan()
# fuwuyuan(guke3)

#写一个猜数字游戏，需求如下：
#随机生成一个0-100之间的数字，让用户猜，如果用户猜对了，提示回答正确，游戏结束
#如果猜错了给出对应的提示，最多允许猜7次
# from random import randint
# num1=randint(0,100)
# for i in range(7):
#     input1=int(input('请输入一个数字:  '))
#     if input1>num1:
#         print('您输入的值过大')
#     elif input1<num1:
#         print('您输入的值过小')
#     else:
#         print('回答正确')
#         break
# else:
#     print('7次都没有猜中，告辞')


#写一个三角形的类
class Sanjiaoxing:
    def __init__(self,a,b,c):
        self.a=a
        self.b=b
        self.c=c
    def zhouchang(self):
        if self.a+self.b<=self.c or self.a+self.c<=self.b or self.b+self.c<=self.a:
            return '无法构成三角形,忽略周长'
        else:
            return self.a+self.b+self.c
    def mianji(self):
        if self.a + self.b <= self.c or self.a + self.c <= self.b or self.b + self.c <= self.a:
            return '无法构成三角形,忽略面积'
        else:
            p=(self.a+self.b+self.c)/2
            return (p*(p-self.a)*(p-self.b)*(p-self.c))**0.5
sjx=Sanjiaoxing(3,4,5)
print(sjx.zhouchang())
print(sjx.mianji())