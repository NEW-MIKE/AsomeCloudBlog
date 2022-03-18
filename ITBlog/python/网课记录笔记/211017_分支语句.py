# 211017_分支语句
#顺序结构,分支结构,循环结构
#python对于缩进有严格的要求,不需要缩进的地方不能随便缩进,需要缩进的地方必须缩进
#python对于缩进几个空格没有严格要求,可以1个,也可以多个,默认一般是缩进4个空格
# if 10>9:
#     print('Hello')
#     print('abc')

#写一个程序,用户输入一个分数,如果大于等于90分,则打印优秀,如果大于等于80分,则打印不错,如果大于等于60分,则打印及格,否则打印不及格
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

#复合条件语句
#如果一个人的年龄大于60岁,并且为男性,我们称他为老先生
# age=62
# gender='male'
# if age>60 and gender=='male':
#     print('老先生')
#
# if age>60:
#     if gender=='male':
#         print('老先生')

#课堂小结
#python的缩进
#input()的用法
#isdigit()的用法
#if else,if elif else
#复合条件判断