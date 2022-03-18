# 211020_循环语句
#一段重复执行的语句,称之为循环语句
#打印从1到10的数字
# i=1
# while i<=10:  #while后面的布尔表达式如果为真,则一直执行循环内的语句,直到布尔表达式的值为假,循环结束
#     print(i)
#     i+=1

#for循环实现从1打印到10
# for i in range(1,11):  #range(起始值,终止值,步长)  包含起始值,不包含终止值,步长默认为1
#     print(i)

#打印100以内的奇数
# for i in range(1,100,2):
#     print(i)
#如果有明确的循环次数,建议用for循环,如果循环次数步确定,建议用while循环,两者可以互相替换

#for循环的起始值,不写时默认为0
# for i in range(10):
#     print(i)

#遍历列表
# list1=['关羽','张飞','赵云','马超','黄忠']
#1.用下标的方式进行遍历
# for i in range(len(list1)):
#     print(list1[i])

#2.直接遍历
# for one in list1:
#     print(one)

#break  终止循环,continue  跳出当次循环
# for i in range(1,11):
#     if i==5:
#         # break
#         continue
#     print(i)
# else:  #循环语句也可以带一个else语句,当循环中没有出现break,则循环结束时,运行一次else中的语句
#     print('循环执行完毕')

#写一个倒计时程序
import time  #加载time模块
for i in range(10,0,-1):
    print(f'\r倒计时{i}秒',end='',flush=True)  #\r光标回到行首
    time.sleep(1)  #让程序等待1秒
else:
    print('\r倒计时结束')