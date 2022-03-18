# 211015_列表与元组
#列表,类似于java中的数组,它比数组更加强大,可以存放任意类型的对象
# listq=[1,'a',[1,2,3],(1,2,3),{'A':'apple'}]
# print(listq)

#打印斐波那契数列的前10位
# list_fbnq=[1,1,2,3,5,8,13,21,34,55]
# print(list_fbnq)

#列表是可变对象,可以增删改
list1=[100,200,300]
# list1.append(600)  #append(),将对象加入到列表的末尾
# list1.insert(1,960)  #将对象加入到列表的指定位置
# list2=[600,800]
# list1.extend(list2)  #将list1和list2拼接到一起
# print(list1)

#列表元素的修改
# list1[0]=999
# print(list1)

#列表元素的删除
# list1.pop()  #pop()默认删除列表的最后一位,也可以指定下标进行删除
# print(list1)

# list1.remove(100)  #remove()直接删除值
# print(list1)

# del list1[0]  #del既可以删除列表中的元素,也可以删除列表
# print(list1)

#列表的切片
# list2=[11,22,33,44,55,66,77,88,99]
# list2_new=list2[0:3]  #列表的切片也是一个新对象
# print(list2_new)

#元组
#元组类似于列表,也可以使用下标和切片,但是元组属于不可变对象,不能增删改
# tuple1=(100,200,300)
#如果元组中只有一个元素,应该加一个逗号
# tuple2=(123,)
# print(type(tuple2))
#如果元组中有子列表,子列表的值是可以修改的
# tuple3=(100,[200,300])
# tuple3[1][0]=680
# print(tuple3)

#课堂小结
#列表的概念,列表是可变对象
#列表的新增append,insert,extend
#列表的删除pop,remove,del
#列表的小标与切片
#元组的概念,元组是不可变对象