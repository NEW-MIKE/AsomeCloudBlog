# 211027_pycharm使用技巧
#新建文件时,自动生成代码
#settings→editor→file and code templates,选择python script
#${DATE}  日期
#${NAME}  文件名

#自动补齐
if __name__ == '__main__':  #先输入main,然后按tab键
    pass
#自动补齐自定义的段落
#settings→editor→live templates,在右侧点击+号,添加自定义的内容
#完成之后,在下方勾选python

#修改注释的颜色
#settings→editor→color scheme→python

#取消语法检查
#settings→editor→inspections,选择python,取消勾选PEP 8两个选项

#分屏
#settings→keymap,查询split关键字,找到分屏的图标,设置快捷键

#切换编码,当遇到乱码问题时,可以考虑切换编码
#settings→editor→file encodings

# list1=[100,98,27,36,-1,56,89,20]
# list1.sort()  #对list1进行永久排序
# print(list1)
# list1_new=sorted(list1)  #sorted()函数,返回排序后的列表
# print(list1_new)

#冒泡算法
#原始数据4321 n个数,第一轮比较n-1次
#3421  #第一次比较,将4和3的位置互换
#3241  #第二次比较,将4和2的位置互换
#3214  #第三次比较,将4和1的位置互换
#原始数据3214,第二轮比较n-2次
#2314  #第一次比较,将3和2的位置互换
#2134  #第二次比较,将3和1的位置互换
#原始数据2134,第三轮比较n-3次
#1234  #第一次比较,将2和1的位置互换

# list2=[100,98,27,36,-1,56,89,20]
# for i in range(len(list2)-1):  #控制比较多少轮
#     for j in range(len(list2)-1-i):  #控制每轮比较多少次
#         if list2[j]>list2[j+1]:  #如果前面的数比后面的大
#             list2[j],list2[j+1]=list2[j+1],list2[j]  #两数互换
# print(list2)

list2=[100,98,27,36,-1,56,89,20]
for i in range(len(list2)-1):  #控制比较多少轮
    for j in range(len(list2)-1-i):  #控制每轮比较多少次
        if list2[j]>list2[j+1]:  #如果前面的数比后面的大
            print(f'第{j}位和第{j+1}位的顺序不对')
            print(f'变化之前------------------>{list2}')
            list2[j],list2[j+1]=list2[j+1],list2[j]  #两数互换
            print(f'变化之后------------------>{list2}')
    print(f'第{i+1}轮比较结束')
print(list2)