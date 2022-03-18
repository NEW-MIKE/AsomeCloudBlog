# 211020_格式化字符串
# a=10
# b=5
# print(str(a)+'+'+str(b)+'='+str(a+b))  #字符串的拼接方式实现
# print('%d+%d=%d'%(a,b,a+b))  #格式化字符串

#方案一
#%s  字符串,%d  整数,%f  浮点数
# info1='我是%s,你是%s,他是%s,今年是%d年'%('德华','天乐','青云',2021)
# print(info1)

#前面的空位比后面的值多,报错
# info1='我是%s,你是%s,他是%s,今年是%d年'%('德华','天乐',2021)
# print(info1)

#前面的空位比后面的值少,报错
# info1='我是%s,你是%s,今年是%d年'%('德华','天乐','青云',2021)
# print(info1)

#前面用%d,后面用字符串,报错
# info1='我是%d'%('德华')

#前面用%s,后面用数字,不报错
# info1='我是%s'%(2021)

#补齐%ns  n是任意整数,不足n位,用空格补齐到n位,默认右对齐,超过n位则全部显示
# info1='我是%5s,你是%5s,他是%s,几年是%010d年'%('高手名字不能太长','天乐','青云',2021)
# print(info1)

#左对齐
# info1='我是%-5s,你是%-5s,他是%-s,几年是%-010d年'%('高手名字不能太长','天乐','青云',2021)
# print(info1)

#%f浮点型,默认保留6位小数
# number1='您输入的数字是%f'%(3.6)
# print(number1)

#保留两位小数
# number1='您输入的数字是%10.2f'%(3.6)
# print(number1)

#方案二
# str1='My name is {},your name is {},age is {}.'.format('Clark','Ralf',20)
# print(str1)

#前面的空位比后面的值多,报错
# str1='My name is {},your name is {},age is {}.'.format('Clark','Ralf')
# print(str1)

#前面的空位比后面的值少,不报错
# str1='My name is {},your name is {},age is {}.'.format('Clark','Ralf',20,36)
# print(str1)

#{}没有写数字时,称为顺序取值法,写了数字,称之为下标取值法
# str1='My name is {1},your name is {0},age is {2}.'.format('Clark','Ralf',20,36)
# print(str1)

#顺序取值法与下标取值法不能混用,否则报错
# str1='My name is {1},your name is {0},age is {}.'.format('Clark','Ralf',20,36)
# print(str1)

#补齐{:n}  n是任意整数,不足n位时补齐到n位,字符串默认左对齐,数字默认右对齐
# str1='My name is {:10},your name is {:10},age is {:10}.'.format('Clark','Ralf',20)
# print(str1)

#左对齐<  居中对齐^  右对齐>
# str1='My name is {:>10},your name is {:^10},age is {:<10}.'.format('Clark','Ralf',20)
# print(str1)

#补0
# str1='My name is {:>10},your name is {:^10},age is {:>010}.'.format('Clark','Ralf',20)
# print(str1)

#在python3.6以后的版本中,方案二有一种简便的写法
# name1='Clark'
# name2='Ralf'
# print(f'My name is {name1:10},your name is {name2:10}')

#课堂小结
#方案一,%s,%d,%f,补齐,对齐方式,前后数量的判断
#方案二,补齐,对齐方式,前后数量的判断,顺序取值法,下标取值法
#f''的用法