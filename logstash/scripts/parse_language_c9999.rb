# frozen_string_literal: true

SOURCE_FIELD = 'target_field'
OPTION_COLUMN = 'lang_exterior_color'
CODE_FIELD = 'code_field'
OPTION_CODE = 'exterior_color_cd'
FIELD_NAME = 'field_name'
OPTION_NAME = 'exterior_color_nm'

def register(params)
  @target_field = params[SOURCE_FIELD]
  @code_field = params[CODE_FIELD]
  @field_name = params[FIELD_NAME]
end

def filter(event)

  code_field = event.get(@code_field)

  # puts "code_field >>> #{code_field}"
  # puts "target_field >>> #{event.get(@target_field)}"
  # puts "field_name >>> #{event.get(@field_name)}"

  if code_field.nil? ||
    code_field.empty? ||
    (code_field == 'C9999') ||
    (code_field == 'C999')
    event.set("[#{@field_name}][srch]", event.get(@target_field))
    event.set("[#{@field_name}][en]", event.get(@target_field))

    return [event]
  end

  pipe_arr = event.get(@target_field).split('|')

  pipe_arr.each do |element|
    language_option = element.split(',')

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
    end
  end

  [event]
end

=begin
docker run --rm \
  -v ${PWD}/scripts:/usr/share/logstash/scripts \
  docker.elastic.co/logstash/logstash:7.17.3 \
  logstash -e "filter { ruby { path => '/usr/share/logstash/scripts/parse_language_c9999.rb' } }" -t
=end
test 'with 2 color languages' do
  parameters do
    {
      SOURCE_FIELD => OPTION_COLUMN,
      CODE_FIELD => OPTION_CODE,
      FIELD_NAME => OPTION_NAME
    }
  end

  in_event do
    {
      OPTION_CODE => 'C020',
      OPTION_COLUMN =>
        'ar,أسود|'\
        'en,Black|'\
        'ko,검정'
    }
  end

  expect('return 3 color results') do |events|
    # puts events.first.get("[#{OPTION_NAME}][ar]")

    # events.first.get("[#{OPTION_NAME}][ar]").match(/Black/) # 아랍어는 검증안되나?
    events.first.get("[#{OPTION_NAME}][en]").match(/Black/)
    events.first.get("[#{OPTION_NAME}][ko]").match(/검정/)
  end
end
