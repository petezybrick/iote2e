'''
Created on Feb 23, 2017

@author: pete.zybrick
'''
import os
from distutils.core import setup

try:
    os.remove('MANIFEST')
except StandardError:
    pass

setup(name='iote2epyclient',
      version='1.0.0',
      description='IoT End to End - Python Client',
      author='Pete Zybrick',
      author_email='pzybrick@gmail.com',
      url='https://github.com/petezybrick/iote2e',
      packages=['iote2epyclient', 'iote2epyclient.launch', 'iote2epyclient.process', 'iote2epyclient.processsim', 'iote2epyclient.pilldispenser', 'iote2epyclient.schema', 'iote2epyclient.test', 'iote2epyclient.ws'],
      package_dir = {'': 'src'},
     )