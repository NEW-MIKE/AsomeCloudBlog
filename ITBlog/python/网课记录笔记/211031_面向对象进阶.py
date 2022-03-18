# 211031_面向对象进阶
# 2021/10/31
class Rectangle:
    def __init__(self,length,width):  #初始化方法
        self.length=length  #将用户传的length转为实例自身的length
        self.width=width  #将用户传的width转为实例自身的width
    def permiter(self):  #实例方法,只能被实例调用,不能被类调用
        return (self.length+self.width)*2
    def area(self):  #实例方法
        return self.length*self.width
    @classmethod  #装饰器,声明下面的方法是类方法
    def features(cls):  #类方法,既可以被实例调用,也可以被类调用
        print('两边的长相等,两边的宽也相等,长和宽的角度为90°')

    @staticmethod  #装饰器,声明下面的方法是静态方法
    def fun1(a, b):  #静态方法,类和实例都可以调用,本质上是函数
        return a + b
rec=Rectangle(5,4)  #实例化
# print(rec.permiter())
# print(rec.area())
# Rectangle.features()
# print(rec.fun1(3,6))
# print(Rectangle.fun1(3,6))

#通过type()查看对象是方法还是函数
# print(type(rec.permiter))  #method
# print(type(rec.features))  #method
# print(type(rec.fun1))  #function

#inspect模块,判断某个对象是否是某种类型,返回值是布尔型
import inspect
# print(inspect.ismethod(rec.permiter))
# print(inspect.isfunction(rec.permiter))
# print(inspect.ismethod(rec.fun1))
# print(inspect.isfunction(rec.fun1))

#继承
#完全继承
# class Square(Rectangle):
#     pass
# squ=Square(6,6)
# print(squ.permiter())
# print(squ.area())

#部分继承,重写一些父类的方法
# class Square(Rectangle):
#     def __init__(self,side):  #方法名与父类方法同名时,覆盖父类的方法
#         self.length=side
#         self.width=side
# squ=Square(6)
# print(squ.permiter())
# print(squ.area())

#继承父类方法的同时,希望能扩展一些代码
# class Square(Rectangle):
#     def __init__(self,side):  #方法名与父类方法同名时,覆盖父类的方法
#         self.length=side
#         self.width=side
#
#     def features(cls):
#         super().features()  #继承父类的features方法的代码
#         print('长和宽也相等')
#
# squ=Square(6)
# print(squ.permiter())
# print(squ.area())
# squ.features()

#@property  声明下面的方法是一个属性,而不是方法
class Test1:
    def __init__(self):
        pass
    @property
    def fun2(self):
        return 'Hello'
tt1=Test1()
print(tt1.fun2)

#课堂小结
#实例方法,类方法,静态方法
#判断是哪种方法,可以用type,也可以用inspect
#继承,部分继承,父类方法的扩展
#了解@classmethod,@staticmethod装饰器
#了解@property装饰器