## 安装Newton版openstack

（官网  https://docs.openstack.org/newton/install-guide-rdo）

### 1.本地docker安装

```)
在本地docker起一个容器   registry.access.redhat.com/rhel7:latest 
```

预配置

 ````)
subscription-manager register --username="glen.lin" --password="LeakOut12"

subscription-manager list --available

subscription-manager attach --pool="8a85f99966f55e5a0166ff9057a07047"

subscription-manager repos --enable=rhel-7-server-optional-rpms --enable=rhel-7-server-extras-rpms --enable=rhel-7-server-rh-common-rpms
 ````

启动缓存并安装yum源

```)
# vi /etc/yum.conf
[main]
cachedir=/var/cache/yum/$basearch/$releasever
keepcache=1

yum install -y https://repos.fedorapeople.org/repos/openstack/openstack-queens/rdo-release-queens-1.noarch.rpm
以上为安装queens版本，由于Newton版本不再维护，故自己创建yum源
在/etc/yum.repos.d目录下创建my.repo(目录底下的repo均会生效)
[rhel-yyyyyyy-yum]
name=Red Hat Enterprise Linux $releasever - $basearch - Source
baseurl=https://buildlogs.centos.org/centos/7/cloud/x86_64/openstack-newton/
enabled=1
gpgcheck=0 

yum makecache  使之生效
```

安装rpm包

```)
yum install -y chrony 【done】
yum install -y mariadb mariadb-server python2-PyMySQL 【done】
yum install -y rabbitmq-server 【done】       {rpm -ivh /temprepo/7Server/openstack-queens/packages/erlang*}
yum install -y memcached python-memcached 【done】
yum install -y etcd 【done】

yum install -y python-openstackclient {rpm -ivh --force xxx}
yum install -y openstack-selinux 【可以不安装】
        修改/etc/selinux/config 文件
        将SELINUX=enforcing改为SELINUX=disabled
        重启机器即可

具体openstack组件rpm:

[keystone]
yum install -y openstack-keystone httpd mod_wsgi

[glance]
yum install -y openstack-glance

[nova]
yum install -y openstack-nova-api openstack-nova-conductor openstack-nova-console openstack-nova-novncproxy openstack-nova-scheduler openstack-nova-placement-api
yum install -y openstack-nova-compute [计算节点安装]
(安装这个的时候遇到一个坑：需要：qemu-kvm-rhev >= 2.9.0
由于无法下载qemu-kvm-rhev，我自己找了一个centos的源http://mirror.centos.org/centos/7/virt/x86_64/kvm-common/
再配一个yum源，yum install -y qemu-kvm-rhev
qemu-kvm-ev-2.12.0-18.el7_6.5.1.x86_64
libvirt-daemon-driver-qemu-4.5.0-10.el7_6.9.x86_64
qemu-kvm-common-ev-2.12.0-18.el7_6.5.1.x86_64
qemu-img-ev-2.12.0-18.el7_6.5.1.x86_64
未免这个源出现问题，用完之后就将他删除了
)

[neutron]
yum install -y openstack-neutron openstack-neutron-ml2 ebtables ipset
# 使用 openstack-neutron-linuxbridge 或者 openstack-neutron-openvswitch. [SRIOV可选]
yum install -y openstack-neutron-linuxbridge
yum install -y openstack-neutron-openvswitch
yum install -y openstack-neutron-sriov-nic-agent

[horizon]
yum install -y openstack-dashboard

[cinder]
# 暂时没有用到：
yum install -y openstack-cinder targetcli python-keystone

[heat]
yum install -y openstack-heat-api openstack-heat-api-cfn openstack-heat-engine

[murano]
yum install -y openstack-murano-agent.noarch openstack-murano-api.noarch openstack-murano-cf-api.noarch openstack-murano-common.noarch openstack
-murano-doc.noarch openstack-murano-engine.noarch openstack-murano-ui.noarch openstack-murano-ui-doc.noarch

查看安装的rpm包：
rpm -qa | grep newton

删除yum安装过的包：
yum remove (上面查的结果)
```

将rpm打包

```)
 ll /var/cache/yum/x86_64/7Server/
 cd /var/cache/yum/x86_64/
 tar -czvf openstack_newton_rpms.tar.gz 7Server/
 docker cp 容器id:/var/cache/yum/x86_64/openstack_newton_rpms.tar.gz .

```



### 2.openstack机器上配yum源

```sh
如何使用openstack_newton_rpms.tar.gz：
拷贝到需要安装OpenStack的机器并解压：
tar -xzvf openstack_newton_rpms.tar.gz -C /var/tmp

配置文件夹为yum源：
createrepo /var/tmp/7Server/

vi /etc/yum.repos.d/local.repo
[local-yum]
name=local-yum
baseurl=file:///var/tmp/7Server/
enabled=1
gpgcheck=0

yum makecache  使之生效

可以使用以下命令来检验yum源是否安装成功
yum clean all
yum repolist
```

### 3.安装基础服务

```sh
#更改hostname
hostnamectl set-hostname controller

[root@localhost ~]# cat /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
<IP> controller

# 关闭防火墙：
[root@controller ~]# systemctl status firewalld.service
[root@controller ~]# systemctl stop firewalld.service
[root@controller ~]# systemctl disable firewalld.service

# 已经开启了ip forward:
[root@controller ~]# sysctl -a | grep net.ipv4.ip_forward
net.ipv4.ip_forward = 1
如果显示的net.ipv4.ip_forward =0，则：
nano /etc/sysctl.conf
取消注释#net.ipv4.ip_forward = 1 或者添加这一行
然后  sysctl -p

yum install -y openstack-selinux(If not installed, neutron-linuxbridge-agent.service cannot start normally.)
yum install -y python-openstackclient
 vi /etc/sysconfig/selinux
SELINUX=disabled
然后reboot(后面httpd没有起来就是因为这个地方没改)

#移除默认的libvirt网络
virsh net-destroy default
virsh net-autostart --disable default
virsh net-undefine default
virsh net-list

sudo vim /etc/my.cnf.d/openstack.cnf
[mysqld]
bind-address = 10.xx.xx.xxx
default-storage-engine = innodb
innodb_file_per_table = on
max_connections = 8192
collation-server = utf8_general_ci
character-set-server = utf8

systemctl enable mariadb
systemctl start mariadb
systemctl status mariadb
mysql_secure_installation
（Enter current password for root (enter for none): enter
You already have a root password set, so you can safely answer 'n'.
Change the root password? [Y/n] n
Remove anonymous users? [Y/n] y
Disallow root login remotely? [Y/n] y
Remove test database and access to it? [Y/n] y
Reload privilege tables now? [Y/n] y
）

# install rabbit:
systemctl enable rabbitmq-server
systemctl start rabbitmq-server

rabbitmqctl add_user openstack xxxxxxx
rabbitmqctl set_permissions openstack ".*" ".*" ".*"

# install memcached:
sudo vim /etc/sysconfig/memcached
OPTIONS="-l 127.0.0.1,::1,controller"

systemctl enable memcached
systemctl start memcached

systemctl enable etcd
systemctl start etcd

# 检查：
[root@controller ~]# rabbitmqctl list_users
Listing users ...
openstack	[]
guest	[administrator]
```

### 4.安装keystone

```sh
参考官网https://docs.openstack.org/newton/install-guide-rdo/keystone-install.html

mysql
CREATE DATABASE keystone;
GRANT ALL PRIVILEGES ON keystone.* TO 'keystone'@'localhost' IDENTIFIED BY 'xxxxxxx';
GRANT ALL PRIVILEGES ON keystone.* TO 'keystone'@'%' IDENTIFIED BY 'xxxxxxx';

vi /etc/keystone/keystone.conf
[database]
# 加上connection
connection = mysql+pymysql://keystone:xxxxxxx@controller/keystone
[token]
provider = fernet

su -s /bin/sh -c "keystone-manage db_sync" keystone
keystone-manage fernet_setup --keystone-user keystone --keystone-group keystone
keystone-manage credential_setup --keystone-user keystone --keystone-group keystone

keystone-manage bootstrap --bootstrap-password xxxxxxx \
  --bootstrap-admin-url http://controller:35357/v3/ \
  --bootstrap-internal-url http://controller:35357/v3/ \
  --bootstrap-public-url http://controller:5000/v3/ \
  --bootstrap-region-id RegionOne
  
vi /etc/httpd/conf/httpd.conf
ServerName controller

ln -s /usr/share/keystone/wsgi-keystone.conf /etc/httpd/conf.d/

systemctl enable httpd.service
systemctl start httpd.service

在/root目录下创建文件adminrc    vi adminrc
export OS_USERNAME=admin
export OS_PASSWORD=xxxxxxx
export OS_PROJECT_NAME=admin
export OS_USER_DOMAIN_NAME=Default
export OS_PROJECT_DOMAIN_NAME=Default
export OS_AUTH_URL=http://controller:35357/v3
export OS_IDENTITY_API_VERSION=3
export OS_IMAGE_API_VERSION=2

openstack project create --domain default \
  --description "Service Project" service
  
openstack project create --domain default \
  --description "Demo Project" demo
  
openstack user create --domain default \
  --password-prompt demo
  
openstack role create user

openstack role add --project demo --user demo user

. adminrc
openstack token issue
+------------+-----------------------------------------------------------------+
| Field      | Value                                                           |
+------------+-----------------------------------------------------------------+
| expires    | 2016-02-12T20:44:35.659723Z                                     |
| id         | gAAAAABWvjYj-Zjfg8WXFaQnUd1DMYTBVrKw4h3fIagi5NoEmh21U72SrRv2trl |
|            | JWFYhLi2_uPR31Igf6A8mH2Rw9kv_bxNo1jbLNPLGzW_u5FC7InFqx0yYtTwa1e |
|            | eq2b0f6-18KZyQhs7F3teAta143kJEWuNEYET-y7u29y0be1_64KYkM7E       |
| project_id | 343d245e850143a096806dfaefa9afdc                                |
| user_id    | ac3377633149401296f6c0d92d79dc16                                |
+------------+-----------------------------------------------------------------+
```

### 5.安装glance

```sh
mysql 
CREATE DATABASE glance;
GRANT ALL PRIVILEGES ON glance.* TO 'glance'@'localhost' IDENTIFIED BY 'xxxxxxx';
GRANT ALL PRIVILEGES ON glance.* TO 'glance'@'%' IDENTIFIED BY 'xxxxxxx';

. adminrc
openstack user create --domain default --password-prompt glance
openstack role add --project service --user glance admin
openstack service create --name glance --description "OpenStack Image" image
openstack endpoint create --region RegionOne image public http://controller:9292
openstack endpoint create --region RegionOne image internal http://controller:9292
openstack endpoint create --region RegionOne image admin http://controller:9292

vi /etc/glance/glance-api.conf
----------------------------------------------------------------------
[database]
connection = mysql+pymysql://glance:xxxxxxx@controller/glance

[keystone_authtoken]
auth_uri = http://controller:5000
auth_url = http://controller:35357
memcached_servers = controller:11211
auth_type = password
project_domain_name = Default
user_domain_name = Default
project_name = service
username = glance
password = xxxxxxx

[paste_deploy]
flavor = keystone

[glance_store]
stores = file,http
default_store = file
filesystem_store_datadir = /var/lib/glance/images/
----------------------------------------------------------------

vi /etc/glance/glance-registry.conf
-----------------------------------------------------------------
[database]
connection = mysql+pymysql://glance:xxxxxxx@controller/glance

[keystone_authtoken]
auth_uri = http://controller:5000
auth_url = http://controller:35357
memcached_servers = controller:11211
auth_type = password
project_domain_name = Default
user_domain_name = Default
project_name = service
username = glance
password = xxxxxxx

[paste_deploy]
flavor = keystone
----------------------------------------------------------------

su -s /bin/sh -c "glance-manage db_sync" glance

systemctl enable openstack-glance-api.service openstack-glance-registry.service
systemctl start openstack-glance-api.service openstack-glance-registry.service

检验安装：
. adminrc
wget http://download.cirros-cloud.net/0.3.4/cirros-0.3.4-x86_64-disk.img
openstack image create "cirros" --file cirros-0.3.4-x86_64-disk.img --disk-format qcow2 --container-format bare --public

openstack image list
+--------------------------------------+--------+--------+
| ID                                   | Name   | Status |
+--------------------------------------+--------+--------+
| 38047887-61a7-41ea-9b49-27987d5e8bb9 | cirros | active |
+--------------------------------------+--------+--------+
```

### 6.安装controller节点nova

```sh
mysql
mysql> CREATE DATABASE nova_api;
mysql> CREATE DATABASE nova;

GRANT ALL PRIVILEGES ON nova_api.* TO 'nova'@'localhost' IDENTIFIED BY 'xxxxxxx';
GRANT ALL PRIVILEGES ON nova_api.* TO 'nova'@'%' IDENTIFIED BY 'xxxxxxx';
GRANT ALL PRIVILEGES ON nova.* TO 'nova'@'localhost' IDENTIFIED BY 'xxxxxxx';
GRANT ALL PRIVILEGES ON nova.* TO 'nova'@'%' IDENTIFIED BY 'xxxxxxx';

vi /etc/nova/nova.conf
-------------------------------------------------------------------
[DEFAULT]
enabled_apis = osapi_compute,metadata

[api_database]
connection = mysql+pymysql://nova:xxxxxxx@controller/nova_api

[database]
connection = mysql+pymysql://nova:xxxxxxx@controller/nova

[DEFAULT]
transport_url = rabbit://openstack:xxxxxxx@controller

[DEFAULT]
auth_strategy = keystone

[keystone_authtoken]
auth_uri = http://controller:5000
auth_url = http://controller:35357
memcached_servers = controller:11211
auth_type = password
project_domain_name = Default
user_domain_name = Default
project_name = service
username = nova
password = xxxxxxx

[DEFAULT]
my_ip = 10.xx.xx.xxx

[DEFAULT]
use_neutron = True
firewall_driver = nova.virt.firewall.NoopFirewallDriver

[vnc]
vncserver_listen = 10.xx.xx.xxx
vncserver_proxyclient_address = 10.xx.xx.xxx

[glance]
api_servers = http://controller:9292

[oslo_concurrency]
lock_path = /var/lib/nova/tmp
----------------------------------------------------------------

su -s /bin/sh -c "nova-manage api_db sync" nova
su -s /bin/sh -c "nova-manage db sync" nova

  systemctl enable openstack-nova-api.service \
  openstack-nova-consoleauth.service openstack-nova-scheduler.service \
  openstack-nova-conductor.service openstack-nova-novncproxy.service
  
  systemctl start openstack-nova-api.service \
  openstack-nova-consoleauth.service openstack-nova-scheduler.service \
  openstack-nova-conductor.service openstack-nova-novncproxy.service

. adminrc
openstack compute service list

+----+--------------------+------------+----------+---------+-------+----------------------------+
| Id | Binary             | Host       | Zone     | Status  | State | Updated At                 |
+----+--------------------+------------+----------+---------+-------+----------------------------+
|  1 | nova-consoleauth   | controller | internal | enabled | up    | 2016-02-09T23:11:15.000000 |
|  2 | nova-scheduler     | controller | internal | enabled | up    | 2016-02-09T23:11:15.000000 |
|  3 | nova-conductor     | controller | internal | enabled | up    | 2016-02-09T23:11:16.000000 |
```

### 7.安装compute节点nova

```sh
安装前准备：
#配置yum源
#更改hostname
hostnamectl set-hostname compute
#配置/etc/hosts（将controller的IP配入，这一步一定要做）
[root@localhost ~]# cat /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
<IP> controller
# 关闭防火墙：
[root@controller ~]# systemctl status firewalld.service
[root@controller ~]# systemctl stop firewalld.service
[root@controller ~]# systemctl disable firewalld.service

#同步两个节点的时间
yum install -y chrony   （详见网络知识9）
--------------------------------------------------------------
服务器端：
[root@controller~]# vim /etc/chrony.conf
  allow 172.25.80.0/24                             ##白名单
#Serve time even if not synchronized to any NTP server.
  local stratum 10                                 ##开启时间同步，设定级别
[root@controller~]# systemctl restart chronyd.service   ##重启服务
[root@controller~]# systemctl enable chronyd.service
客户端：
vim /etc/chrony.conf
server 10.xx.xx.xxx  iburst                       ##配置时间服务器地址
systemctl restart chronyd.service
chronyc sources -v               ##查看时间信息
MS Name/IP address         Stratum Poll Reach LastRx Last sample
——————————————————---————————————————
————————————————————————————————
^* 172.25.80.100                10   6    17    48    +18us[  +25us] +/-  212us
^表示服务，*代表同步成功，?代表未同步
如果一直是？，等一会就好了
--------------------------------------------------------------

yum install -y openstack-selinux(If not installed, neutron-linuxbridge-agent.service cannot start normally.)
yum install -y python-openstackclient
 vi /etc/sysconfig/selinux
SELINUX=disabled
然后reboot

yum install openstack-nova-compute

vi /etc/nova/nova.conf
--------------------------------------------------------------------
[DEFAULT]
enabled_apis = osapi_compute,metadata

[DEFAULT]
transport_url = rabbit://openstack:xxxxxxx@controller

[DEFAULT]
auth_strategy = keystone

[keystone_authtoken]
auth_uri = http://controller:5000
auth_url = http://controller:35357
memcached_servers = controller:11211
auth_type = password
project_domain_name = Default
user_domain_name = Default
project_name = service
username = nova
password = xxxxxxx

[DEFAULT]
my_ip = 10.xx.xx.xxx

[DEFAULT]
use_neutron = True
firewall_driver = nova.virt.firewall.NoopFirewallDriver

[vnc]
enabled = True
vncserver_listen = 0.0.0.0
vncserver_proxyclient_address = 10.xx.xx.xxx
novncproxy_base_url = http://10.xx.xx.xxx:6080/vnc_auto.html
(这里用IP，不用controller，不然instance的console登不上)

[glance]
api_servers = http://controller:9292

[oslo_concurrency]
lock_path = /var/lib/nova/tmp
-------------------------------------------------------------------

egrep -c '(vmx|svm)' /proc/cpuinfo
如果此命令返回一或更大的值，则计算节点支持硬件加速，通常不需要额外配置.如果该命令返回的值为零，则计算节点不支持硬件加速，必须配置libvirt来使用QEMU而不是KVM. vi /etc/nova/nova.conf   
[libvirt]
virt_type = qemu

systemctl enable libvirtd.service openstack-nova-compute.service
systemctl start libvirtd.service openstack-nova-compute.service
(启动不成功看/var/log/nova/nova-compute.log，如果有如下显示AMQP server on controller:5672 is unreachable 关闭两个节点的防火墙，并尝试如下操作，虽然不一定有效)
 # iptables -I INPUT -p tcp --dport 5672 -j ACCEPT      添加规则
 # service iptables save      保存设置
 # service iptables restart     重启iptables，生效规则
https://blog.csdn.net/moolight_shadow/article/details/46507705
https://blog.csdn.net/shion0305/article/details/62237649
https://blog.csdn.net/controllerha/article/details/78821152

. adminrc

openstack compute service list
+----+--------------------+------------+----------+---------+-------+------------------
| Id | Binary             | Host       | Zone     | Status  | State | Updated At       
+----+--------------------+------------+----------+---------+-------+------------------
|  1 | nova-consoleauth   | controller | internal | enabled | up    | 2016-02-09T23:11:15.000000 |
|  2 | nova-scheduler     | controller | internal | enabled | up    | 2016-02-09T23:11:15.000000 |
|  3 | nova-conductor     | controller | internal | enabled | up    | 2016-02-09T23:11:16.000000 |
|  4 | nova-compute       | compute1   | nova     | enabled | up    | 2016-02-09T23:11:20.000000 |
+----+--------------------+------------+----------+---------+-------+------------------

```

### 8.安装controller节点neutron

```sh
mysql
CREATE DATABASE neutron;
GRANT ALL PRIVILEGES ON neutron.* TO 'neutron'@'localhost' IDENTIFIED BY 'xxxxxxx';
GRANT ALL PRIVILEGES ON neutron.* TO 'neutron'@'%' IDENTIFIED BY 'xxxxxxx';

. adminrc
openstack user create --domain default --password-prompt neutron
openstack role add --project service --user neutron admin
openstack service create --name neutron --description "OpenStack Networking" network
openstack endpoint create --region RegionOne network public http://controller:9696
openstack endpoint create --region RegionOne network internal http://controller:9696
openstack endpoint create --region RegionOne network admin http://controller:9696


网络问题由于第二种selfservice完全覆盖第一种，只用安装第二种
-----------------------------------------------------------------------------------
#Networking Option 1: Provider networks
yum install openstack-neutron openstack-neutron-ml2 \
  openstack-neutron-linuxbridge ebtables
vi /etc/neutron/neutron.conf
[database]
connection = mysql+pymysql://neutron:xxxxxxx@controller/neutron

[DEFAULT]
core_plugin = ml2
service_plugins =

[DEFAULT]
transport_url = rabbit://openstack:xxxxxxx@controller

[DEFAULT]
auth_strategy = keystone

[keystone_authtoken]
auth_uri = http://controller:5000
auth_url = http://controller:35357
memcached_servers = controller:11211
auth_type = password
project_domain_name = Default
user_domain_name = Default
project_name = service
username = neutron
password = xxxxxxx

[DEFAULT]
notify_nova_on_port_status_changes = True
notify_nova_on_port_data_changes = True

[nova]
auth_url = http://controller:35357
auth_type = password
project_domain_name = Default
user_domain_name = Default
region_name = RegionOne
project_name = service
username = nova
password = xxxxxxx

[oslo_concurrency]
lock_path = /var/lib/neutron/tmp

vi /etc/neutron/plugins/ml2/ml2_conf.ini
[ml2]
type_drivers = flat,vlan

[ml2]
tenant_network_types =

[ml2]
mechanism_drivers = linuxbridge

[ml2]
extension_drivers = port_security

[ml2_type_flat]
flat_networks = provider

[securitygroup]
enable_ipset = True

vi /etc/neutron/plugins/ml2/linuxbridge_agent.ini
[linux_bridge]
physical_interface_mappings = provider:vlan7

[vxlan]
enable_vxlan = False

[securitygroup]
enable_security_group = True
firewall_driver = neutron.agent.linux.iptables_firewall.IptablesFirewallDriver

vi /etc/neutron/dhcp_agent.ini
[DEFAULT]
interface_driver = neutron.agent.linux.interface.BridgeInterfaceDriver
dhcp_driver = neutron.agent.linux.dhcp.Dnsmasq
enable_isolated_metadata = True
-----------------------------------------------------------------------------


--------------------------------------------------------------------------
#Networking Option 2: Self-service networks
yum install openstack-neutron openstack-neutron-ml2 openstack-neutron-linuxbridge ebtables
vi /etc/neutron/neutron.conf
[database]
connection = mysql+pymysql://neutron:xxxxxxx@controller/neutron

[DEFAULT]
core_plugin = ml2
service_plugins = router
allow_overlapping_ips = True

[DEFAULT]
transport_url = rabbit://openstack:xxxxxxx@controller

[DEFAULT]
auth_strategy = keystone

[keystone_authtoken]
auth_uri = http://controller:5000
auth_url = http://controller:35357
memcached_servers = controller:11211
auth_type = password
project_domain_name = Default
user_domain_name = Default
project_name = service
username = neutron
password = xxxxxxx

[DEFAULT]
notify_nova_on_port_status_changes = True
notify_nova_on_port_data_changes = True

[nova]
auth_url = http://controller:35357
auth_type = password
project_domain_name = Default
user_domain_name = Default
region_name = RegionOne
project_name = service
username = nova
password = xxxxxxx

[oslo_concurrency]
lock_path = /var/lib/neutron/tmp


vi /etc/neutron/plugins/ml2/ml2_conf.ini
[ml2]
type_drivers = flat,vlan,vxlan

[ml2]
tenant_network_types = vxlan

[ml2]
mechanism_drivers = linuxbridge,l2population

[ml2]
extension_drivers = port_security

[ml2_type_flat]
flat_networks = provider

[securitygroup]
enable_ipset = True

[ml2_type_vxlan]
vni_ranges = 1:1000

[securitygroup]
enable_ipset = True


vi /etc/neutron/plugins/ml2/linuxbridge_agent.ini
[linux_bridge]
physical_interface_mappings = provider:vlan7
（此处vlan7最好填一张不用的网卡，用于创建provider网络）

[vxlan]
enable_vxlan = True
local_ip = 10.xx.xx.xxx
l2_population = True

[securitygroup]
enable_security_group = True
firewall_driver = neutron.agent.linux.iptables_firewall.IptablesFirewallDriver

vi /etc/neutron/l3_agent.ini
[DEFAULT]
interface_driver = neutron.agent.linux.interface.BridgeInterfaceDriver

vi /etc/neutron/dhcp_agent.ini
[DEFAULT]
interface_driver = neutron.agent.linux.interface.BridgeInterfaceDriver
dhcp_driver = neutron.agent.linux.dhcp.Dnsmasq
enable_isolated_metadata = True

网络配置结束
--------------------------------------------------------------------------



vi /etc/neutron/metadata_agent.ini
--------------------------------------------------------------
[DEFAULT]
nova_metadata_ip = controller
metadata_proxy_shared_secret = xxxxxxx

[neutron]
url = http://controller:9696
auth_url = http://controller:35357
auth_type = password
project_domain_name = Default
user_domain_name = Default
region_name = RegionOne
project_name = service
username = neutron
password = xxxxxxx
service_metadata_proxy = True
metadata_proxy_shared_secret = xxxxxxx

ln -s /etc/neutron/plugins/ml2/ml2_conf.ini /etc/neutron/plugin.ini

su -s /bin/sh -c "neutron-db-manage --config-file /etc/neutron/neutron.conf --config-file /etc/neutron/plugins/ml2/ml2_conf.ini upgrade head" neutron

systemctl restart openstack-nova-api.service

systemctl enable neutron-server.service neutron-linuxbridge-agent.service neutron-dhcp-agent.service neutron-metadata-agent.service
systemctl start neutron-server.service neutron-linuxbridge-agent.service neutron-dhcp-agent.service neutron-metadata-agent.service
systemctl restart neutron-server.service neutron-linuxbridge-agent.service neutron-dhcp-agent.service neutron-metadata-agent.service

systemctl enable neutron-l3-agent.service
systemctl start neutron-l3-agent.service

.adminrc
neutron ext-list

openstack network agent list
```

### 9.安装compute节点neutron

```sh
yum install openstack-neutron-linuxbridge ebtables ipset

vi /etc/neutron/neutron.conf
-----------------------------------------------------------------
[DEFAULT]
transport_url = rabbit://openstack:xxxxxxx@controller

[DEFAULT]
auth_strategy = keystone

[keystone_authtoken]
auth_uri = http://controller:5000
auth_url = http://controller:35357
memcached_servers = controller:11211
auth_type = password
project_domain_name = Default
user_domain_name = Default
project_name = service
username = neutron
password = xxxxxxx

[oslo_concurrency]
lock_path = /var/lib/neutron/tmp
---------------------------------------------------------------------

#Configure networking options
vi /etc/neutron/plugins/ml2/linuxbridge_agent.ini
-------------------------------------------------------------------------------
[linux_bridge]
physical_interface_mappings = provider:vlan7

[vxlan]
enable_vxlan = True
local_ip = 10.136.40.xx
l2_population = True

[securitygroup]
enable_security_group = True
firewall_driver = neutron.agent.linux.iptables_firewall.IptablesFirewallDriver
---------------------------------------------------------------------------------

vi /etc/nova/nova.conf
--------------------------------------------------------
[neutron]
url = http://controller:9696
auth_url = http://controller:35357
auth_type = password
project_domain_name = Default
user_domain_name = Default
region_name = RegionOne
project_name = service
username = neutron
password = xxxxxxx
---------------------------------------------------------

systemctl restart openstack-nova-compute.service

systemctl enable neutron-linuxbridge-agent.service
systemctl start neutron-linuxbridge-agent.service

systemctl restart neutron-linuxbridge-agent.service

. adminrc

neutron ext-list

openstack network agent list
+--------------------------------------+--------------------+------------+-------------------+-------+-------+---------------------------+
| ID                                   | Agent Type         | Host       | Availability Zone | Alive | State | Binary                    |
+--------------------------------------+--------------------+------------+-------------------+-------+-------+---------------------------+
| 0400c2f6-4d3b-44bc-89fa-99093432f3bf | Metadata agent     | controller | None              | True  | UP    | neutron-metadata-agent    |
| 83cf853d-a2f2-450a-99d7-e9c6fc08f4c3 | DHCP agent         | controller | nova              | True  | UP    | neutron-dhcp-agent        |
| ec302e51-6101-43cf-9f19-88a78613cbee | Linux bridge agent | compute    | None              | True  | UP    | neutron-linuxbridge-agent |
| fcb9bc6e-22b1-43bc-9054-272dd517d025 | Linux bridge agent | controller | None              | True  | UP    | neutron-linuxbridge-agent |
+--------------------------------------+--------------------+------------+-------------
```

### 10.安装dashboard

```sh
yum install openstack-dashboard

vi /etc/httpd/conf.d/openstack-dashboard.conf
在WSGISocketPrefix run/wsgi下面加一行代码：
WSGIApplicationGroup %{GLOBAL}

vi /etc/openstack-dashboard/local_settings
---------------------------------------------------------------
OPENSTACK_HOST = "controller"
ALLOWED_HOSTS = ['*', ]

搜索memcached
SESSION_ENGINE = 'django.contrib.sessions.backends.cache'
CACHES = {
    'default': {
         'BACKEND': 'django.core.cache.backends.memcached.MemcachedCache',
         'LOCATION': 'controller:11211',
    }
}
并注释掉默认的

OPENSTACK_KEYSTONE_URL = "http://%s:5000/v3" % OPENSTACK_HOST

OPENSTACK_KEYSTONE_MULTIDOMAIN_SUPPORT = True

OPENSTACK_API_VERSIONS = {
    "identity": 3,
    "image": 2,
    "volume": 2,
}

OPENSTACK_KEYSTONE_DEFAULT_DOMAIN = "default"

OPENSTACK_KEYSTONE_DEFAULT_ROLE = "user"

OPENSTACK_NEUTRON_NETWORK = {
    ...
    'enable_router': False,
    'enable_quotas': False,
    'enable_distributed_router': False,
    'enable_ha_router': False,
    'enable_lb': False,
    'enable_firewall': False,
    'enable_vpn': False,
    'enable_fip_topology_check': False,
}

TIME_ZONE = "UTC"

systemctl restart httpd.service memcached.service

出错查看/var/log/httpd/error_log
```

在dashboard上创建network和instance

```sh
#显示创建页面
Provider Network Type 选择 “Flat”(这里选flat，因为linuxbridge_agent.ini[linux_bridge]
physical_interface_mappings = provider:vlan7，如果要选择创建vlan类型，则 provider:eno?实际网卡名)
Physical Network 填写 “provider”，与 ml2_conf.ini 中 flat_networks 参数值保持一致。 
勾选shared、External Network
点击 “Create Network”，flat_net 创建成功。 
点击 flat_net 链接，进入 network 配置页面，目前还没有 subnet，点击 “Create Subnet” 按钮。 
设置 IP 地址为 “10.136.40.0/22” gw "10.136.40.1" 范围自己定义10.136.41.190,10.136.41.199 
点击 “Create”，subnet 创建成功
provider网络创建完成之后，ifconfig看到vlan7的IP被openstack管理起来了
(如果为br,则不会被管理起来)

在页面创建路由，选择provider网络

创建vxlan的网络，Physical Network填default，
设置 IP 地址为 “192.168.2.0/24” gw"192.168.2.1" 范围自己定义192.168.2.2,192.168.2.100

#创建flavor
openstack flavor create --id 0 --vcpus 1 --ram 64 --disk 1 m1.nano
```

### 11.安装controller的cinder

```sh
mysql
CREATE DATABASE cinder;
GRANT ALL PRIVILEGES ON cinder.* TO 'cinder'@'localhost' IDENTIFIED BY 'xxxxxxx';
GRANT ALL PRIVILEGES ON cinder.* TO 'cinder'@'%' IDENTIFIED BY 'xxxxxxx';

. adminrc
openstack user create --domain default --password-prompt cinder
openstack role add --project service --user cinder admin
openstack service create --name cinder --description "OpenStack Block Storage" volume
openstack service create --name cinderv2 --description "OpenStack Block Storage" volumev2
openstack endpoint create --region RegionOne volume public http://controller:8776/v1/%\(tenant_id\)s
openstack endpoint create --region RegionOne volume internal http://controller:8776/v1/%\(tenant_id\)s
openstack endpoint create --region RegionOne volume admin http://controller:8776/v1/%\(tenant_id\)s
openstack endpoint create --region RegionOne volumev2 public http://controller:8776/v2/%\(tenant_id\)s
openstack endpoint create --region RegionOne volumev2 internal http://controller:8776/v2/%\(tenant_id\)s  
openstack endpoint create --region RegionOne volumev2 admin http://controller:8776/v2/%\(tenant_id\)s

yum install openstack-cinder

vi /etc/cinder/cinder.conf
-----------------------------------------------------------------
[database]
connection = mysql+pymysql://cinder:xxxxxxx@controller/cinder

[DEFAULT]
transport_url = rabbit://openstack:xxxxxxx@controller

[DEFAULT]
auth_strategy = keystone

[keystone_authtoken]
auth_uri = http://controller:5000
auth_url = http://controller:35357
memcached_servers = controller:11211
auth_type = password
project_domain_name = Default
user_domain_name = Default
project_name = service
username = cinder
password = xxxxxxx

[DEFAULT]
my_ip = 10.xx.xx.xxx

[oslo_concurrency]
lock_path = /var/lib/cinder/tmp
----------------------------------------------------------------------

su -s /bin/sh -c "cinder-manage db sync" cinder

vi /etc/nova/nova.conf
[cinder]
os_region_name = RegionOne

systemctl restart openstack-nova-api.service

systemctl enable openstack-cinder-api.service openstack-cinder-scheduler.service
systemctl start openstack-cinder-api.service openstack-cinder-scheduler.service
```

### 12.安装compute的cinder

```sh
yum install lvm2
systemctl enable lvm2-lvmetad.service
systemctl start lvm2-lvmetad.service
```

```sh
#若机器没有分盘可采用虚拟盘方案，如果分盘依据官网https://docs.openstack.org/newton/install-guide-rdo/cinder-storage-install.html）
#虚拟盘
dd if=/dev/zero of=/vol/cinder-volumes bs=1 count=0 seek=500G   
# Mount the file.   
loopdev=`losetup -f`   
losetup $loopdev /vol/cinder-volumes   
# Initialize as a physical volume.   
pvcreate $loopdev   
# Create the volume group.   
vgcreate cinder-volumes $loopdev   
# Verify the volume has been created correctly.   
pvscan 

[root@compute vol]# vgs
  VG             #PV #LV #SN Attr   VSize    VFree   
  cinder-volumes   1   6   0 wz--n- <500.00g <440.00g
  rhel             1   1   0 wz--n-    2.18t    4.00m

[root@compute vol]# lvs
  LV                                          VG             Attr       LSize  Pool Origin Data%  Meta%  Move Log Cpy%Sync Convert
  volume-166d8b9b-6b47-44fe-a418-fe9d08dc64a8 cinder-volumes -wi-ao---- 10.00g                                                    
  volume-1bd5f83e-c535-43a5-8842-c4b7a4b80d7d cinder-volumes -wi-ao---- 10.00g                                                    
  volume-a066c141-fa32-4cab-9ede-d16bd4b74150 cinder-volumes -wi-ao---- 10.00g                                                    
  volume-b77e8891-aaee-4516-8a32-2319290075c1 cinder-volumes -wi-ao---- 10.00g                                                    
  volume-ccdc6b06-fa3d-4d09-9e74-fd483fda516a cinder-volumes -wi-ao---- 10.00g                                                    
  volume-ee3d3c0d-fa52-440e-b7bc-782575d9a874 cinder-volumes -wi-ao---- 10.00g                                                    
  root                                        rhel           -wi-ao----  2.18t 
```

```sh
yum install openstack-cinder targetcli python-keystone

vi /etc/cinder/cinder.conf
------------------------------------------------------------------
[database]
connection = mysql+pymysql://cinder:xxxxxxx@controller/cinder

[DEFAULT]
transport_url = rabbit://openstack:xxxxxxx@controller

[DEFAULT]
auth_strategy = keystone

[keystone_authtoken]
auth_uri = http://controller:5000
auth_url = http://controller:35357
memcached_servers = controller:11211
auth_type = password
project_domain_name = Default
user_domain_name = Default
project_name = service
username = cinder
password = xxxxxxx

[DEFAULT]
my_ip = 10.xx.xx.xxx

[lvm]
volume_driver = cinder.volume.drivers.lvm.LVMVolumeDriver
volume_group = cinder-volumes
iscsi_protocol = iscsi
iscsi_helper = lioadm

[DEFAULT]
enabled_backends = lvm

[DEFAULT]
glance_api_servers = http://controller:9292

[oslo_concurrency]
lock_path = /var/lib/cinder/tmp
-------------------------------------------------------------------

systemctl enable openstack-cinder-volume.service target.service
systemctl start openstack-cinder-volume.service target.service

. adminrc
openstack volume service list
+------------------+------------+------+---------+-------+----------------------------+
| Binary           | Host       | Zone | Status  | State | Updated_at                 |
+------------------+------------+------+---------+-------+----------------------------+
| cinder-scheduler | controller | nova | enabled | up    | 2016-09-30T02:27:41.000000 |
| cinder-volume    | block@lvm  | nova | enabled | up    | 2016-09-30T02:27:46.000000 |
+------------------+------------+------+---------+-------+----------------------------+
```

```sh
挂载完后看磁盘  df -h      或者   fdisk -l
```

### 13.安装heat

```sh
mysql
CREATE DATABASE heat;
GRANT ALL PRIVILEGES ON heat.* TO 'heat'@'localhost' IDENTIFIED BY 'xxxxxxx';
GRANT ALL PRIVILEGES ON heat.* TO 'heat'@'%' IDENTIFIED BY 'xxxxxxx';

vi /etc/heat/heat.conf 
---------------------------------------------------------------
[database]
connection = mysql+pymysql://heat:xxxxxxx@controller/heat

[DEFAULT]
rpc_backend = rabbit

[oslo_messaging_rabbit]
rabbit_host = controller
rabbit_userid = openstack
rabbit_password = xxxxxxx

[keystone_authtoken]
auth_uri = http://controller:5000
auth_url = http://controller:35357
memcached_servers = controller:11211
auth_type = password
project_domain_name = default
user_domain_name = default
project_name = service
username = heat
password = xxxxxxx

[trustee]
auth_type = password
auth_url = http://controller:35357
username = heat
password = xxxxxxx
user_domain_name = default

[clients_keystone]
auth_uri = http://controller:35357

[ec2authtoken]
auth_uri = http://controller:5000

[DEFAULT]
heat_metadata_server_url = http://controller:8000
heat_waitcondition_server_url = http://controller:8000/v1/waitcondition

[DEFAULT]
stack_domain_admin = heat_domain_admin
stack_domain_admin_password = xxxxxxx
stack_user_domain_name = heat
-----------------------------------------------------------------------------

systemctl enable openstack-heat-api.service openstack-heat-api-cfn.service openstack-heat-engine.service
systemctl start openstack-heat-api.service openstack-heat-api-cfn.service openstack-heat-engine.service
```

### 14.openstack常用命令

```sh
#查看nova服务
openstack compute service list
#查看网络服务
openstack network agent list
#查看stack
openstack stack list
#创建stack
heat stack-create -f xxxx_base.yaml -e xxxx_env.yaml media
#查看instance
openstack server list

neutron router-list
neutron router-delete [id]
```

### 15.安装错误

```sh
1.安装第一步av节点安装不成功，waitcondition timeout
看对应安装在哪台机子上,进入那台机器，查看nova和neutron的log
如果有too many openfile 的错误，重启neutron服务
systemctl restart neutron-linuxbridge-agent.service

2.check状态不过时，可能与cpu个数有关，试着加大cpu
```

### 16.openstack配置sriov

```sh
官方配置：https://docs.openstack.org/neutron/stein/admin/config-sriov.html

https://www.sunmite.com/openstack/openstack-sriov.html

```

### 17.openstack 有关flavor配置

```sh
https://docs.openstack.org/nova/latest/user/flavors.html
```

## 18.网络相关知识

Provider network 只对 Flat 和 VLAN 类型的网络才有意义，因为 Provider network 的一个重要属性是 provider:physical_network，而这个参数对其他网络类型没有有意义。并且provider网络都是规划层面的事情，有管理员动手操作。

 

 Tenant network 是由 tenant 的普通用户创建的网络。默认情况下，这类用户不能创建共享的 tenant network（因此 Nuetron Server 的policy 设置了"create_network:shared": "rule:admin_only"。），因此这种网络是完全隔离的，也不可以被别的 tenant 共享。openstack里对于有些API的操作进行了相关权限控制，也是结合他的模型场景来确定的。Tenant network 也有 local，flat，vlan，gre 和 vxlan 等类型。但是，tenant 普通用户创建的 Flat 和 VLAN tenant network 实际上还是 Provider network，所以真正有意义的是 GRE 和 VXLAN 类型，这种网络和物理网络没有绑定关系。