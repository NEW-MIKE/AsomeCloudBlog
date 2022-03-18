# 211101_面向对象思考题讲解
#解题思路
#新建一个老虎的类,老虎的体重是200,类里面有叫的方法,有吃的方法
#新建一个羊的类,羊的体重是100,类里面有叫的方法,有吃的方法
#新建一个房间的类
#将老虎或羊的实例放入房间的实例
#写游戏相关的代码①调用叫的方法②调用吃的方法③游戏时间的控制④游戏结束时显示每个房间的动物的种类与体重

class Tiger:
    def __init__(self):
        self.name='老虎'
        self.weight=200
    def eat(self,food):
        if food=='meat':
            print('喂食正确')
            self.weight+=10
        elif food=='grass':
            print('喂食错误')
            self.weight-=10
    def roar(self):
        print('Wow!!')
        self.weight-=5

class Sheep:
    def __init__(self):
        self.name='羊'
        self.weight=100
    def eat(self,food):
        if food=='grass':
            print('喂食正确')
            self.weight+=10
        elif food=='meat':
            print('喂食错误')
            self.weight-=10
    def roar(self):
        print('mie~~')
        self.weight-=5

class Room:
    def __init__(self,animal):
        self.animal=animal

roomlist=[]  #定义一个列表,用来存放房间的实例

from random import randint
for i in range(10):
    if randint(1,2)==1:  #在1和2之间随机取一个值
        animal=Tiger()  #实例化一个老虎
    else:
        animal=Sheep()  #实例化一个羊
    room=Room(animal)  #实例化一个房间,把动物的实例放进去
    roomlist.append(room)  #把房间的实例放到列表中

import time
start_time=time.time()  #记录游戏开始的时间
while time.time()-start_time<=10:
    room_number=randint(0,9)  #随机选取一个房间
    random_room=roomlist[room_number]  #选取该房间
    load1=input(f'当前访问的是{room_number+1}号房间,请问是敲门还是喂食?1表示敲门,2表示喂食')
    if load1=='1':
        random_room.animal.roar()  #调用房间实例的动物实例的叫的方法
    elif load1=='2':
        food=input('请选择喂的食物  meat/grass')
        if food in ('meat','grass'):
            random_room.animal.eat(food)  #调用房间实例的动物实例的吃的方法
        else:
            print('您输入的食物不正确')
    else:
        print('请输入1或2')
else:
    print('游戏结束')
    for i in range(len(roomlist)):
        print(f'{i+1}号房间的动物是{roomlist[i].animal.name},体重是{roomlist[i].animal.weight}')