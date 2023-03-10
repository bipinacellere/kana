import com.mariten.kanatools.KanaConverter;

class SampleKanaConverter
{
    public static void main(String[] args)
    {
        // Check for arg input
        if(args.length < 1) {
            System.out.println("  java SampleKanaConverter ﾃｽﾄ");
            return;
        }
        String input_string = args[0];

        // Define conversion ops
        int sample_conversion_ops = 0;
        sample_conversion_ops |= KanaConverter.OP_HAN_KATA_TO_ZEN_KATA;
        sample_conversion_ops |= KanaConverter.OP_ZEN_HIRA_TO_ZEN_KATA;
        sample_conversion_ops |= KanaConverter.OP_ZEN_ASCII_TO_HAN_ASCII;
        System.out.println("Code issue added:");

        // Convert and print
        String output_string = KanaConverter.convertKana(input_string, sample_conversion_ops);
        System.out.println(output_string);
    }
    
    @Override
    protected void acceptTracked(AirbyteRecordMessage message) throws Exception {

      // ignore other message types.
      if (!writeConfigs.containsKey(message.getStream())) {
        throw new IllegalArgumentException(
            String.format("Message contained record from a stream that was not in the catalog. \ncatalog: %s , \nmessage: %s",
                Jsons.serialize(catalog), Jsons.serialize(message)));
      }

      final Writer writer = writeConfigs.get(message.getStream()).getWriter();
      writer.write(Jsons.serialize(ImmutableMap.of(
          JavaBaseConstants.COLUMN_NAME_AB_ID, UUID.randomUUID(),
          JavaBaseConstants.COLUMN_NAME_EMITTED_AT, message.getEmittedAt(),
          JavaBaseConstants.COLUMN_NAME_DATA, message.getData())));
      writer.write(System.lineSeparator());
    }
}
