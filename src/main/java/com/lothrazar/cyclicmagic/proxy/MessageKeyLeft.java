package com.lothrazar.cyclicmagic.proxy;
  
import com.lothrazar.cyclicmagic.PlayerPowerups;
import com.lothrazar.cyclicmagic.SpellRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf; 
import net.minecraft.entity.player.EntityPlayer; 

public class MessageKeyLeft implements IMessage, IMessageHandler<MessageKeyLeft, IMessage>
{
	public static final int ID = 1;
	public MessageKeyLeft()
	{ 
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
	}
	
	@Override
	public IMessage onMessage(MessageKeyLeft message, MessageContext ctx)
	{  
		EntityPlayer player = ctx.getServerHandler().playerEntity; 
		PlayerPowerups props = PlayerPowerups.get(player);
	 
		//www.minecraftforge.net/forum/index.php/topic,20135.0.html
 
		if(props.getSpellToggle() != SpellRegistry.SPELL_TOGGLE_HIDE)
		{
			SpellRegistry.shiftLeft(player);
		}
 
		return null;
	}
}
 