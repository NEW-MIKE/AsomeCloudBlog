# 211022_字典与json
#字典是以键值对形式出现的存储对象
#姓名:关羽
#攻击力:90
#防御力:89
#精神力:92
#统率:87
dict1={'姓名':'关羽','攻击力':90,'防御力':89,'统率':87}

#字典的键可以存放字符串,数字,元组,字典的值可以存放任意对象
# dict2={'A':{'a':'ace'}}
# print(dict2)

#字典是无序的
# dicta={'A':'apple','B':'book'}
# dictb={'B':'book','A':'apple'}
# print(dicta==dictb)

#字典的键是唯一的
# dictc={'A':'apple','A':'ace'}
# print(dictc)

#字典的新增与修改语句是一样的,根据键修改值,已有键则更新,否则新增
dictd={'A':'apple','B':'book'}
# dictd['A']='ace'
# dictd['C']='cake'
# dictd.update({'D':'duck','E':'earth'})
# print(dictd)

#删除字典中的键值对
# del dictd['A']
# print(dictd)

#清空字典
# print(id(dictd))
# # dictd.clear()  #清空字典,id不变
# dictd={}  #重新赋值了一个空字典,id变了
# print(id(dictd))

#遍历字典
# for k in dict1.keys():  #遍历键
#     print(k)

# for v in dict1.values():  #遍历值
#     print(v)

# for k,v in dict1.items():
#     print(k,v)

#判断某个对象是否在字典中,根据键判断,而不是值
# dict10={'A':'apple'}
# print('apple' in dict10)
str1='''{
"aac003": "张三",
"aac030": "13574553997",
"crm003": "1",
"crm004": "1"
}'''

import json
from random import randint
str1_new=json.loads(str1)  #将json转为字典
str1_new['aac030']=f'138{randint(10000000,99999999)}'
str1=json.dumps(str1_new)  #将字典转为json
print(str1)


#课堂小结
#字典的概念
# 字典是无序的
# 字典的键是唯一的
# 字典是可变对象
#遍历字典
#json.loads(),json.dumps()