#ES+IK分词器
# Author: yiuman
FROM elastic/elasticsearch:7.14.0

MAINTAINER YiumanKam

ENV VERSION=7.14.0

ADD https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v${VERSION}/elasticsearch-analysis-ik-$VERSION.zip /tmp/
#ADD ./elasticsearch-analysis-ik-$VERSION.zip /tmp/
RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install --batch file:///tmp/elasticsearch-analysis-ik-$VERSION.zip

RUN rm -rf /tmp/*

#打包运行  没墙的情况下 分词建议下载到本地 不然慢死你
#docker build -t es-ik:7.14.0 .
#docker run -d -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" --name es-ik es-ik:7.14.0