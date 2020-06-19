package learn.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty服务端
 * @author bat
 * @date 2020/6/19
 */
public class NettyServer {

    public static void main(String[] args){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup parentExecutors = new NioEventLoopGroup(1);
        //线程数默认处理器两倍 NettyRuntime.availableProcessors() * 2
        EventLoopGroup childExecutors = new NioEventLoopGroup();
        try {
            serverBootstrap.group(parentExecutors,childExecutors);
            serverBootstrap.channel(NioServerSocketChannel.class);
            //设置大小
            serverBootstrap.option(ChannelOption.SO_BACKLOG,128)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });
            //绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(88).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            parentExecutors.shutdownGracefully();
            childExecutors.shutdownGracefully();
        }


    }

}
