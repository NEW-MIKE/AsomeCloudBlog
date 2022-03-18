# 211024_思考题讲解2
# (签到时间①,课程id②,学生id③)
# {
#     ③: [
#         {'lessonid': ②, 'checkintime': ①},
#         {'lessonid': ②, 'checkintime': ①}
#     ],
#     ③: [
#         {'lessonid': ②, 'checkintime': ①}
#     ]
# }


#解题思路
#第一步,分析需求,预期实现什么效果
#第二步,提取出原始字符串中的①②③
#第三步,按照题目要求,将①②③放入对应的位置

# dict1={}  #外层字典
# dict2={}  #内层字典
# with open('D:/0005_1.txt') as file1:
#     list1=file1.read().splitlines()  #读取文件内容,返回值是列表,每一行是一个元素
#     for one in list1:
#         one1=one.replace('(','').replace(')','').replace('\t','').replace("'",'').strip(',')
#         checkintime,lessonid,studentid=one1.split(',')  #取得签到时间,课程id,学生id
#         lessonid=lessonid.strip()
#         studentid=studentid.strip()
#         dict2={'lessonid':lessonid,'checkintime':checkintime}  #将签到信息放入内层字典
#         if studentid not in dict1:  #如果学生id不在外层字典中
#             dict1[studentid]=[]  #生成学生id和空列表的键值对
#         dict1[studentid].append(dict2)  #将dict2放入到dict1的子列表中
#
# import pprint
# pprint.pprint(dict1)

#第五次课思考题2
# name: Jack   ;    salary:  12000
#  name :Mike ; salary:  12300
# name: Luk ;   salary:  10030
#   name :Tim ;  salary:   9000
# name: John ;    salary:  12000
# name: Lisa ;    salary:   11000

# name: Jack   ;    salary:  12000 ;  tax: 1200 ; income:  10800
# name: Mike   ;    salary:  12300 ;  tax: 1230 ; income:  11070
# name: Luk    ;    salary:  10030 ;  tax: 1003 ; income:   9027
# name: Tim    ;    salary:   9000 ;  tax:  900 ; income:   8100
# name: John   ;    salary:  12000 ;  tax: 1200 ; income:  10800
# name: Lisa   ;    salary:  11000 ;  tax: 1100 ; income:   9900

#解题思路
#第一步,分许需要提取的数据
#第二步,按照题目要求的格式写入文件

with open('D:/第5次课练习题2数据.txt') as file1,open('D:/file2.txt','w+') as file2:
    file_lines=file1.read().splitlines()
    for one in file_lines:
        one1,one2=one.split(';')
        name=one1.split(':')[1].strip()  #取得name的值
        salary=int(one2.split(':')[1].strip())
        file2.write(f'name: {name:7};    salary:  {salary:5} ;  tax: {int(salary*0.1):4} ; income:  {int(salary*0.9):5}\n')