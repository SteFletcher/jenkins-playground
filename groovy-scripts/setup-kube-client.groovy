import org.csanchez.jenkins.plugins.kubernetes.*
import jenkins.model.*

def j = Jenkins.getInstance()

def k = new KubernetesCloud(
  'jenkins-test-2',
  null,
  'https://192.168.99.100:8443',
  'myproject',
  'http://97.1.175.166:8090/',
  '10', 0, 0, 5
)
k.setSkipTlsVerify(true)
k.setCredentialsId('openshift-provisioning-account')

def p = new PodTemplate()
p.setName('openshift-agent-myproject')
p.setLabel('Linux')
p.setInstanceCap(1)
p.idleMinutes=20
k.addTemplate(p)

def containers = []
def c1 = new ContainerTemplate('jnlp', 'jenkinsci/jnlp-slave', '', '${computer.jnlpmac} ${computer.name}')
c1.setTtyEnabled(false)
containers.add(c1)

p.setContainers(containers)

k.addTemplate(p)

j.clouds.add(k)
j.save()
