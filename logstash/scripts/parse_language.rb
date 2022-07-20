# frozen_string_literal: true

SOURCE_FIELD = 'target_field'
TARGET_OPTION_INFO = 'target_option_info'
FIELD_NAME = 'field_name'
OPTION_NAME = 'option_nm'

def register(params)
  @target_field = params[SOURCE_FIELD]
  @field_name = params[FIELD_NAME]
end

def filter(event)

  pipe_arr = event.get(@target_field).split('|')

  pipe_arr.each do |element|
    language_option = element.split(',')
    # puts tmp
    # puts tmp.size

    if language_option.empty?
      event.set("[#{@field_name}][srch]", nil)
      event.set("[#{@field_name}][en]", nil)
    elsif language_option.size == 1
      event.set("[#{@field_name}][srch]", language_option[0])
      event.set("[#{@field_name}][en]", language_option[0])
    elsif (language_option.size > 1) && (language_option[0] == 'en')
      event.set("[#{@field_name}][srch]", language_option[1])
      event.set("[#{@field_name}][en]", language_option[1])
    else
      event.set("[#{@field_name}][#{language_option[0]}]", language_option[1])
      # puts event.get("[#{@field_name}][#{language_option[0]}]")
    end
  end

  puts '%s >>> %s' % ['languages', event.get("[#{@field_name}]")]
  # puts event.get("[#{@field_name}]").size

  [event]
end

=begin
docker run --rm \
  -v ${PWD}/scripts:/usr/share/logstash/scripts \
  docker.elastic.co/logstash/logstash:7.17.3 \
  logstash -e "filter { ruby { path => '/usr/share/logstash/scripts/parse_language.rb' } }" -t
=end
test 'with 5 languages' do
  parameters do
    {
      SOURCE_FIELD => TARGET_OPTION_INFO,
      FIELD_NAME => OPTION_NAME
    }
  end

  in_event do
    {
      TARGET_OPTION_INFO =>
        'ar,مؤقت|'\
        'en,Temporary|'\
        'ja,臨時|'\
        'ka,დროებითი|'\
        'ko,임시'
    }
  end

  expect('return 6 results') do |events|
    # event = events.first
    events.first.get("[#{OPTION_NAME}][srch]").match(/Temporary/) # 추가
    events.first.get("[#{OPTION_NAME}][ar]").match(/مؤقت/)
    events.first.get("[#{OPTION_NAME}][en]").match(/Temporary/)
    events.first.get("[#{OPTION_NAME}][ja]").match(/臨時/)
    events.first.get("[#{OPTION_NAME}][ka]").match(/დროებითი/)
    events.first.get("[#{OPTION_NAME}][ko]").match(/임시/)
  end
end
