# 211025_yaml文件操作
import yaml
with open('config.yaml') as file1:
    neirong=yaml.load(file1,Loader=yaml.FullLoader)
    print(neirong)

#yaml的书写规范,列表必须要有空格,可以是一个或多个

# with open('config.yaml',encoding='utf-8') as file1:
#     neirong=yaml.load_all(file1,Loader=yaml.FullLoader)
#     for one in neirong:
#         print(one)