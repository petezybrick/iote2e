# Copyright 2016, 2017 Peter Zybrick and others.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""
ClientUtils
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import datetime

class ClientUtils(object):
    '''
    Utility Methods
    '''

    @staticmethod
    def nowIso8601():
        (dt, micro) = datetime.datetime.utcnow().strftime('%Y-%m-%dT%H:%M:%S.%f').split('.')
        dt = "%s.%03dZ" % (dt, int(micro) / 1000)
        return dt
