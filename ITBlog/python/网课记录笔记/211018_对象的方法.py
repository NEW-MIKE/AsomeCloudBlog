# 211018_对象的方法
#方法其实就是函数,只不过是写在类里面的
# str1='abcdefccccccg'
# print(str1.index('c',3))  #返回某个或某些字符在字符串中的位置,默认从头开始查找,也可以指定起始位置进行查找
#index()方法,找不到值抛异常,find()方法,找不到值返回-1
# print(str1.find('q',3))

#strip()  #去掉字符串前后的空格,也可以指定其他字符
# str2='               hbgvfds   cdt   gry   nhbg                     '
# str2_new='********hgtqrfwed*********'
# print(str2.strip())
# print(str2_new.strip('*'))

# replace(oldstr,newstr)  #oldstr需要替换的字符,newstr替换后的字符,返回值是str型
# str3='ABCDEFCDGYNBCDKHLYBTRGECDHYBGTG5TRBGF'
# str3_new=str3.replace('CD','德华').replace('F','学友')
# print(str3_new)

#startswith()  判断字符串是否以某个或某些字符开头,返回值是布尔型
#根据身份证号判断是否是南京的身份证
# id_card='32010419990928097X'
# if id_card.startswith('3201'):
#     print('南京的身份证')

#endswith()  判断字符串是否以某个或某些字符结尾,返回值是布尔型
#判断身份证的最后一位是否是X
# if id_card.endswith('X'):
#     print('最后一位是X')

#split()  切割字符串,它有一个参数,以参数作为分隔符,将字符串分隔为多个值,返回值是列表
# str9='abcdefg'
# print(str9.split('c'))

#如果切割符位于首位或末尾,会产生空值
# str9_new='gabcdefg'
# print(str9_new.split('g'))

#课堂小结
#index(),find()
#strip()
#replace()
#startswith(),endswith()
#split()

#上节课思考题
#写一个号段筛选程序,需求如下:
#用户从控制台输入一个手机号,判断出运营商(移动(130-150),联通(151-170),电信(171-190))
#如果用户输入的位数不对,提示用户位数有误,如果用户输入非数字,提示有非法字符
# input1=input('请输入一个手机号:  ')
# if not input1.isdigit():  #判断用户输入的是否不是纯数字,如果不是,则提示用户输入的值不正确
#     print('您输入的不是数字')
# else:
#     if len(input1)!=11:  #如果用户输入的位数不是11位,提示位数不正确
#         print('位数不正确')
#     else:
#         num1=int(input1[0:3])  #取得手机号前三位,并转为int型
#         if 130<=num1<=150:
#             print('移动')
#         elif 151<=num1<=170:
#             print('联通')
#         elif 171<=num1<=190:
#             print('电信')
#         else:
#             print('您输入的手机号不属于任何运营商')