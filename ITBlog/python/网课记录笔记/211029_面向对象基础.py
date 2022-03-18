# 211029_面向对象基础
#面向过程与面向对象的区别
#假如写一个五子棋程序,面向过程的方式,先判断谁落子,然后判断是否获胜,如果没有获胜继续判断谁落子,判断是否获胜
#面向对象的方式,事先写好各种方法,主程序只负责调用,调用判断落子的方法,调用判断获胜的方法
#类和实例的概念
#类是抽象的模板,实例是根据模板创建出来的具体的对象,比如人类就是一个类,吴彦祖是人类的一个实例
#长方形
class Rectangle:
    list1=[10,20,30,40,50]  #类属性,具有唯一性
    def __init__(self,length,width):  #初始化方法
        self.length=length  #将用户传的length转为实例自身的length
        self.width=width  #将用户传的width转为实例自身的width
        self.list2 = [10, 20, 30, 40, 50,60]  # 实例属性,不唯一
    def permiter(self):  #周长方法
        return (self.length+self.width)*2
    def area(self):  #面积方法
        return self.length*self.width
# rec=Rectangle(5,4)  #实例化
# print(rec.permiter())
# print(rec.area())
# print(rec.__dict__)  #查看实例的属性
# print(rec.list2)

# rec1=Rectangle(10,8)
# rec2=Rectangle(8,6)
# print(id(rec1.list1)==id(rec2.list1))  #所有的实例,共用一个list1
# print(id(rec1.list2)==id(rec2.list2))  #每个实例,拥有各自的属性

#单例模式
#一般情况下,类可以生成任意个实例,单例模式只生成一个
# class Single:
#     def __init__(self):  #初始化方法
#         pass
#     def __new__(cls, *args, **kwargs):  #构造方法,用于生成实例
#         if not hasattr(cls,'obj'):  #判断类当中有没有实例方法,如果没有则新建
#             cls.obj=object.__new__(cls)  #生成实例对象
#         return cls.obj  #返回实例

#反射
# class Fanguan:
#     price = 18
#     def yuxiangrousi(self):
#         return '鱼香肉丝'
#     def gongbaojiding(self):
#         return '宫爆鸡丁'
#     def qingjiaotudousi(self):
#         return '青椒土豆丝'
#     def shousibaocai(self):
#         return '手撕包菜'
#     def kaishuibaicai(self):
#         return '开水白菜'
# fg=Fanguan()
# while True:
#     caidan=input('请点菜:  ')
#     if hasattr(fg,caidan):  #hasattr(对象,属性或方法名),判断对象中是否有某个属性或某个方法,返回值是布尔型
#         print('好的,请厨师开始做菜')
#         break
#     else:
#         print('没有这道菜')

# def fun1():
#     return '没有这道菜'
#
# input1=input('请输入要翻译的菜名:  ')
# c=getattr(fg,input1,fun1)  #getattr(对象,属性或方法名,缺省值),判断对象中是否有某个属性或某个方法,返回对象本身,如果找不到对象,返回缺省值
# print(c())
# print(fg.price)
# setattr(fg,'price',[1,2,3,4,5])  #setattr(对象,属性或方法名,新值),将对象中的属性改为新的值
# print(fg.price)