# test_pytest211103
#pytest是一个自动化测试框架,向下兼容unittest
#pytest命名规范
#文件名以test_开头,或者_test结尾
#类以Test开头,且类当中不能有__init__方法
#方法或函数以test_开头
#断言必须使用assert

# assert 预期结果==实际结果

import os
import pytest
import allure
# def test_c01():
#     print('开始执行用例1')
#     assert 1==2
# def test_c02():
#     print('开始执行用例2')
#     assert 2==3

# class Test1:
#     def setup_method(self):  #①
#         print('正在进行初始化工作')
#     def test_c01(self):  #②
#         assert 1==1
#     def test_c02(self):  #③
#         assert 2==3
#     def teardown_method(self):  #④
#         print('正在进行收尾工作')
#method级别运行顺序①②④①③④

# class Test1:
#     def setup_class(self):  #①
#         print('正在进行初始化工作')
#     def test_c01(self):  #②
#         assert 1==1
#     def test_c02(self):  #③
#         assert 2==3
#     def teardown_class(self):  #④
#         print('正在进行收尾工作')

# list1=[[10,22],[2**25,33554432],[72,72]]
# @allure.feature('层级1')
# @allure.story('层级2')
# @allure.title('层级3')
# class Test10:
#     @pytest.mark.parametrize('result,real_result',list1)
#     def test_10(self,result,real_result):
#         assert result==real_result
#class级别运行顺序①②③④
#module级别,整个模块只运行一次

# 1、下载allure.zip
# 2、解压allure.zip到一个文件目录中
# 3、将allure报告安装目录\bin所在的路径添加环境变量path中
# 4、pip install  allure-pytest

#测试结果"."表示测试通过,测试结果F表示测试不通过
# if __name__ == '__main__':
#     pytest.main(['test_pytest211103.py','-s','--alluredir','./report'])  #-s表示运行执行print语句
#     os.system('allure generate ./report -o ./report/report --clean')

#九九乘法表
with open('D:/99乘法表_211103.txt','w+') as file1:
    for i in range(1,10):
        for j in range(1,i+1):
            file1.write(f'{j}*{i}={i*j}\t')
        file1.write('\n')