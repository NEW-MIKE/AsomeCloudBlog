# test_example1
import pytest
@pytest.fixture(scope='class')  #装饰器,声明下面的函数是setup函数
#scope缺省值为function,还有class,module,session级别
#session级别,fixture的内容写到conftest.py文件中,目录内的所有文件共用这个配置
def fun1():
    print('测试开始')
    yield  #声明下面的代码是teardown的用法
    print('测试结束')
class Test1:
    def test_some_data(self,fun1):
        assert 1==2
    def test_some_data2(self,fun1):
        assert 2==3
if __name__ == '__main__':
    pytest.main(['test_example1.py','-s'])